# [Scala]AES暗号化/復号化を行う簡単なスクリプト

## 概要

AES暗号の簡単な実装です。CBCのためパスワード（鍵）の他に初期化ベクトルをパラメータとして使います。

## 動作環境

* Scala (新しめ)
* Java 8 (java.util.Base64を使用のため)

## ライセンス

MITライセンス

## 使い方

```>scala SimpleAES.scala [options] [password] [IV] [INPUTFILE] [OUTPUTFILE]```

      [options] -e or --encrypt : encryptmode
                -d or --decrypt : decryptmode

例：

```>scala SimpleAES.scala -e 0123456789012345 abcdefghijklmnop in.txt crypt.txt```

```>scala SimpleAES.scala -d 0123456789012345 abcdefghijklmnop crypt.txt out.txt```

## 説明

```-e``` 指定時に暗号化して ```-d``` 指定時に復号化します。

passwordとIVの両パラメータはどんな文字列を指定しても内部的に16文字になります。足りない場合は"0"を足し、多い場合は先頭16文字を使います。

暗号化の処理の部分は、ほぼ```javax.crypto```パッケージを使っていますのでjavaの実装のそれと変わりません。

暗号化後、テキスト形式で出力するためにBASE64形式にします。出力ファイルの上書き確認は行っておりませんのでご注意ください。


## その他

今回はAES128ビットでの実装です。256ビットで利用するにはJavaの無制限強度ポリシファイルを導入する必要があるので、環境構築後そのまま実行できる最長の鍵長なはずです。

実用には暗号化前に圧縮処理などを追加するとよりよいでしょう。また、UTF-8エンコード以外のテキストファイルや巨大なファイル対策などしてないのでその点は考慮してください。（```scala.io.Source```で一文字ずつ読んでいるのは凄く重そう...）
