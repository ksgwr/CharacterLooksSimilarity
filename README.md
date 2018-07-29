# CharacterLooksSimilarity [![Build Status](https://travis-ci.org/ksgwr/CharacterLooksSimilarity.svg?branch=master)](https://travis-ci.org/ksgwr/CharacterLooksSimilarity)

文字素性を2値画像化して表現します。その類似度をS/N比で計算することで似た文字を抽出することを想定します。日本語では「斉藤」「齊藤」など読み方は同じで複数の書き方があり、日本語と中国語では「黒」「黑」の様な繁体字の違いが存在します。

## サンプル

```java
int[][] a = CharacterLooksSimilarity.createCharacterLooks("a");
int[][] b = CharacterLooksSimilarity.createCharacterLooks("b");

float sim = CharacterLooksSimilarity.calcSimilarity(a, b); // 0.83
```

## TODO

類似度をS/N比で計算していますが単純な一致率では想定する結果になりません。文字列に適した類似度を定義する必要があります。

```
=== 近くならなくても良い文字
斉	㚎	0.90873015
斉	㪯	0.9166667
斉	䓬	0.9007937
斉	备	0.9047619
斉	奄	0.9007937
斉	宵	0.9166667
斉	寊	0.9126984
斉	斍	0.9444444
=== 近くなって欲しい文字
齋	斎	0.8769841
斉	齊	0.90873015
斉	齋	0.79761904
齋	齊	0.8333333
斉	斎	0.8333333
斎	齊	0.78174603
```
