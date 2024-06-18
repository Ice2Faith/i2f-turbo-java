# basex

- basex 系列是一种编码方法
- 宗旨是将由 8bit 的 byte 字节，转换为其他 bit 位数的 byte 的表示方式
- 主要是为了将不可见字符或者非 ASCII 字符转换为 ASCII 字符进行传输
- 其中 base64 使用最为广泛

## base64

- 64 个字符
- 需要 2^6 表示，6 bit 表示即可
- 寻找 6bit 和 8bit 最小公倍数，为 24
- 则24/8=3byte转换为24/6=4个编码byte表示
- encode:

```java
    byte[] src =[123412,34 1234,1234 12,341234]step+=3
byte[] dst[4]=0;
dst[0]=(src[0]>>>2)&0x03f;
dst[1]=((src[0]&0x03)<<4)|((src[1]>>>4)&0x0f);
dst[2]=((src[1]&0x0f)<<2)|((src[2]>>>6)&0x03);
dst[3]=src[2]&0x03f;
```

- decode:

```java
    byte[] src =[00123412 0034,1234 001234,12 00341234]step+=4
byte[] dst[3]=0;
dst[0]=((src[0]&0x03f)<<2)|((src[1]>>>4)&0x03);
dst[1]=((src[1]&0x0f)<<4)|((src[2]>>>2)&0x0f);
dst[2]=((src[2]&0x03)<<6)|(src[3]&0x03f);
```

## base32

- 32 字符
- 需要 2^5 表示， 5 bit 表示即可
- 寻找 5bit 和 8bit 最小公倍数，为 40
- 则40/8=5byte转换为40/5=8个编码byte表示
- encode:

```java
    byte[] src =[12341,234 12,34123,4 1234,1234 1,23412,34 123,41234]step+=5
byte[] dst[8]=0;
dst[0]=(src[0]>>>3)&0x01f;
dst[1]=((src[0]&0x07)<<2)|((src[1]>>6)&0x03);
dst[2]=(src[1]>>>1)&0x01f;
dst[3]=((src[1]&0x01)<<4)|((src[2]>>>4)&0x0f);
dst[4]=((src[2]&0x0f)<<1)|((src[3]>>>7)&0x01);
dst[5]=(src[3]>>2)&0x01f;
dst[6]=((src[3]&0x03)<<3)|((src[4]>>>5)&0x07);
dst[7]=src[4]&0x01f;
```

- decode:

```java
    byte[] src =[00012341 000234,12 00034123 0004,1234 0001234,1 00023412 00034,123 00041234]step+=8
byte[] dst[5]=0;
dst[0]=((src[0]&0x01f)<<3)|((src[1]>>2)&0x07);
dst[1]=((src[1]&0x03)<<6)|((src[2]&0x01f)<<1)|((src[3]>>>4)&0x01);
dst[2]=((src[3]&0x0f)<<4)|((src[4]>>1)&0x0f);
dst[3]=((src[4]&0x01)<<7)|((src[5]&0x01f)<<2)|((src[6]>>>3)&0x03);
dst[4]=((src[6]&0x07)<<5)|(src[7]&0x01f);
```

## base16

- 16 字符
- 寻找 2^4 表示， 4 bit 表示即可
- 寻找 4bit 和 8bit 最小公倍数，为 8
- 则8/8=1byte转换为8/4=2个编码byte表示
- encode:

```java
    byte[] src =[1234,1234]step+=1
byte[] dst[2]=0;
dst[0]=(src[0]>>>4)&0x0f;
dst[1]=(src[0]&0x0f);
```

- decode:

```java
    byte[] src =[00001234,00001234,]
byte[] dst[1]=0;
dst[0]=((src[0]&0x0f)<<4)|(src[1]&0x0f);
```

## RFC3548 规范

- 官方链接

```shell
https://www.rfc-editor.org/rfc/rfc3548.html
```

- base64 映射字符

```shell
Table 1: The Base 64 Alphabet

Value Encoding  Value Encoding  Value Encoding  Value Encoding
  0 A            17 R            34 i            51 z
  1 B            18 S            35 j            52 0
  2 C            19 T            36 k            53 1
  3 D            20 U            37 l            54 2
  4 E            21 V            38 m            55 3
  5 F            22 W            39 n            56 4
  6 G            23 X            40 o            57 5
  7 H            24 Y            41 p            58 6
  8 I            25 Z            42 q            59 7
  9 J            26 a            43 r            60 8
 10 K            27 b            44 s            61 9
 11 L            28 c            45 t            62 +
 12 M            29 d            46 u            63 /
 13 N            30 e            47 v
 14 O            31 f            48 w         (pad) =
 15 P            32 g            49 x
 16 Q            33 h            50 y
```

- base64 URL/FileName 映射字符

```shell
Table 2: The "URL and Filename safe" Base 64 Alphabet

Value Encoding  Value Encoding  Value Encoding  Value Encoding
   0 A            17 R            34 i            51 z
   1 B            18 S            35 j            52 0
   2 C            19 T            36 k            53 1
   3 D            20 U            37 l            54 2
   4 E            21 V            38 m            55 3
   5 F            22 W            39 n            56 4
   6 G            23 X            40 o            57 5
   7 H            24 Y            41 p            58 6
   8 I            25 Z            42 q            59 7
   9 J            26 a            43 r            60 8
  10 K            27 b            44 s            61 9
  11 L            28 c            45 t            62 - (minus)
  12 M            29 d            46 u            63 _ (understrike)
  13 N            30 e            47 v
  14 O            31 f            48 w         (pad) =
  15 P            32 g            49 x
  16 Q            33 h            50 y
```

- base32 映射字符

```shell
Table 3: The Base 32 Alphabet

Value Encoding  Value Encoding  Value Encoding  Value Encoding
    0 A             9 J            18 S            27 3
    1 B            10 K            19 T            28 4
    2 C            11 L            20 U            29 5
    3 D            12 M            21 V            30 6
    4 E            13 N            22 W            31 7
    5 F            14 O            23 X
    6 G            15 P            24 Y         (pad) =
    7 H            16 Q            25 Z
    8 I            17 R            26 2
```

- base16 映射字符

```shell
Table 5: The Base 16 Alphabet

Value Encoding  Value Encoding  Value Encoding  Value Encoding
  0 0             4 4             8 8            12 C
  1 1             5 5             9 9            13 D
  2 2             6 6            10 A            14 E
  3 3             7 7            11 B            15 F
```
