package jp.ksgwr.charlooksim;

import java.io.BufferedWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class SimilarCharacterPairExtractor {

    private final int consumerSize;

    private final BlockingQueue<CharPair> queue;

    private final ExecutorService executor;

    public SimilarCharacterPairExtractor(int consumerSize, ExecutorService executor) {
        this.consumerSize = consumerSize;
        this.queue = new ArrayBlockingQueue<>(consumerSize + 2);
        this.executor = executor;
    }

    public void extract(char[] candidates, PrintStream out, float simThreshold) throws ExecutionException, InterruptedException {
        extract(new CandidatesCharacterProducer(queue, consumerSize, candidates), out, simThreshold);
    }

    public void extract(char target, char start, char end, PrintStream out, float simThreshold) throws ExecutionException, InterruptedException {
        extract(new TargetCharacterProducer(queue, consumerSize, target, start, end), out, simThreshold);
    }

    public void extract(char start, char end, PrintStream out, float simThreshold) throws ExecutionException, InterruptedException {
        extract(new CharacterPowerSetProducer(queue, start, end, consumerSize), out, simThreshold);
    }

    public void extract(Callable<Void> producer, PrintStream out, float simThreshold) throws ExecutionException, InterruptedException {
        List<Future<Void>> futures = new ArrayList<Future<Void>>(consumerSize+1);

        futures.add(executor.submit(producer));
        for (int i=0;i<consumerSize;i++) {
            futures.add(executor.submit(new CharPairSimilarityConsumer(queue, out, simThreshold)));
        }

        // wait for task complete
        for (Future<Void> future: futures) {
            future.get();
        }
        executor.shutdown();
    }

    public static class CharPair {
        String a;
        String b;

        public CharPair(String a, String b) {
            this.a = a;
            this.b = b;
        }
    }

    public static abstract class CharPairProducer implements Callable<Void> {
        protected BlockingQueue<CharPair> queue;
        protected int consumerSize;

        protected CharPairProducer(BlockingQueue queue, int consumerSize) {
            this.queue = queue;
            this.consumerSize = consumerSize;
        }

        @Override
        public Void call() throws Exception {
            produce();

            for (int i=0;i<consumerSize;i++) {
                queue.put(new CharPair(null, null));
            }

            return null;
        }

        public abstract void produce() throws Exception;

    }

    public static class CharacterPowerSetProducer extends CharPairProducer {
        private char start;

        private char end;

        public CharacterPowerSetProducer(BlockingQueue<CharPair> queue, char start, char end, int consumerSize) {
            super(queue, consumerSize);
            this.start = start;
            this.end = end;
        }

        @Override
        public void produce() throws Exception {
            for (char i=start;i<end;i++) {
                String a = String.valueOf(i);
                for (char j=(char)(i+1);j<=end;j++) {
                    String b = String.valueOf(j);
                    queue.put(new CharPair(a, b));
                }
            }
        }
    }

    public static class TargetCharacterProducer extends CharPairProducer {
        private char target;
        private char start;
        private char end;
        public TargetCharacterProducer(BlockingQueue queue, int consumerSize, char target, char start, char end) {
            super(queue, consumerSize);
            this.target = target;
            this.start = start;
            this.end = end;
        }

        @Override
        public void produce() throws Exception {
            String a = String.valueOf(target);
            for (char i=start;i<end;i++) {
                if (target != i) {
                    String b = String.valueOf(i);
                    queue.put(new CharPair(a, b));
                }
            }
        }
    }

    public static class CandidatesCharacterProducer extends CharPairProducer {
        private char[] candidates;

        public CandidatesCharacterProducer(BlockingQueue queue, int consumerSize, char[] candidates) {
            super(queue, consumerSize);
            this.candidates = candidates;
        }

        @Override
        public void produce() throws Exception {
            for (int i = 0; i < candidates.length; i++) {
                String a = String.valueOf(candidates[i]);
                for (int j = i + 1; j <= candidates.length - 1; j++) {
                    String b = String.valueOf(candidates[j]);
                    queue.put(new CharPair(a, b));
                }
            }
        }
    }

    public static class CharPairSimilarityConsumer implements Callable<Void> {
        private BlockingQueue<CharPair> queue;

        private PrintStream out;

        private float simThreshold;

        public CharPairSimilarityConsumer(BlockingQueue<CharPair> queue, PrintStream out, float simThreshold) {
            this.queue = queue;
            this.out = out;
            this.simThreshold = simThreshold;
        }

        @Override
        public Void call() throws Exception {
            CharPair pair;
            while((pair = queue.take()) != null) {
                if (pair.a == null || pair.b == null) {
                    break;
                }
                int[][] a = CharacterLooksSimilarity.createCharacterLooks(pair.a);
                int[][] b = CharacterLooksSimilarity.createCharacterLooks(pair.b);

                float sim = CharacterLooksSimilarity.calcSimilarity(a, b);

                if (sim >= simThreshold) {
                    out.println(pair.a + "\t" + pair.b + "\t" + sim);
                }
            }
            return null;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        int n = Runtime.getRuntime().availableProcessors();
        SimilarCharacterPairExtractor extractor = new SimilarCharacterPairExtractor(n, Executors.newFixedThreadPool(n + 2));

        //extractor.extract('a', 'z', System.out, 0.97f);
        /*
b	h	0.9727891
b	p	0.9727891
d	q	0.9727891
e	o	0.9727891
h	n	0.9863946
i	l	0.984127
v	y	0.9809524
         */
        //extractor.extract('\u3400', '\u9FFF', System.out, 0.95f);
        //extractor.extract('斉','\u3400', '\u9FFF', System.out, 0.9f);
        /*
斉	㚎	0.90873015
斉	㪯	0.9166667
斉	䓬	0.9007937
斉	备	0.9047619
斉	奄	0.9007937
斉	宵	0.9166667
斉	寊	0.9126984
斉	斍	0.9444444
         */
        extractor.extract(new char[]{'斉', '齋', '斎', '齊'}, System.out, 0.5f);
        /*
齋	斎	0.8769841
斉	齊	0.90873015
斉	齋	0.79761904
齋	齊	0.8333333
斉	斎	0.8333333
斎	齊	0.78174603
         */

    }
}
