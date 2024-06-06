# spring-retry-recover

# Spring Retry ve Recover Kullanarak Hata Yonetimi

Modern yazilim sistemlerinde, ozellikle mikroservis mimarilerinde, gecici hatalar (ornegin, ag baglanti sorunlari veya gecici hizmet kesintileri) siklikla karsilasilan durumlardir. Bu tur gecici hatalari yonetmek ve uygulamanizin guvenilirligini artirmak icin yeniden deneme (retry) mekanizmalari kullanilir. Spring Retry, bu tur mekanizmalari Spring uygulamalarinda kolayca uygulamak icin tasarlanmis guclu bir kutuphanedir. Ayrica, Spring Retry'nin `Recover` islevi, tum denemeler basarisiz oldugunda uygulamanizin nasil tepki verecegini belirlemenize olanak tanir.

## Spring Retry ve Recover Nedir?

### Spring Retry
Spring Retry, belirli bir islem basarisiz oldugunda, bu islemi otomatik olarak belirli sayida tekrar denemenize olanak tanir. Ornegin, bir veritabani baglantisi kurmaya calistiginizda bir hata alirsaniz, Spring Retry bu baglanti denemesini otomatik olarak tekrar eder. Boylece, gecici hatalarla basa cikabilir ve islemlerin basarili olma olasiligini artirabilirsiniz.

### Spring Recover
Spring Recover ise tum yeniden deneme girisimleri basarisiz oldugunda devreye giren bir mekanizmadir. Recover islevi, hata durumlarinda alternatif bir islem veya hata yonetimi stratejisi sunar. Bu, uygulamanizin beklenmeyen durumlarla daha zarif bir sekilde basa cikmasini saglar.


### Metot Aciklamalari

`retryOrderProcess` -> Bu metot, siparis detaylari bos ise `SQLException` firlatir. Eger `SQLException` meydana gelirse, metot uc kez yeniden denenir. Uc basarisiz denemeden sonra, `recoverOrderProcess` metodu devreye girer ve bir kurtarma islemi yapar.

`retryOrderProcessWithBackoff` -> Bu metot da, siparis detaylari bos ise `SQLException` firlatir. `SQLException` durumunda, metot maksimum bes kez yeniden denenir. Her yeniden denemede bekleme suresi baslangicta 2 saniye olup, her seferinde iki katina cikarilacak sekilde artar. Maksimum bekleme suresi 30 saniyedir. Bes basarisiz denemeden sonra, `recoverOrderProcessWithBackoff` metodu devreye girer ve bir kurtarma islemi yapar.


### Request ve Log Ciktilari

#### `GET http://localhost:8080/retryOrderProcess`

```
2024-06-07T10:30:17.253+03:00  INFO --- OrderService : Throwing SQLException in method processOrder()
2024-06-07T10:30:18.259+03:00  INFO --- OrderService : Throwing SQLException in method processOrder()
2024-06-07T10:30:19.262+03:00  INFO --- OrderService : Throwing SQLException in method processOrder()
2024-06-07T10:30:19.269+03:00  INFO --- OrderService : In recoverOrderProcess method
```

#### `GET http://localhost:8080/retryOrderProcessWithBackoff`

```
2024-06-07T10:31:04.555+03:00  INFO --- OrderService : Throwing SQLException in method processOrderWithBackoff()
2024-06-07T10:31:06.560+03:00  INFO --- OrderService : Throwing SQLException in method processOrderWithBackoff()
2024-06-07T10:31:10.566+03:00  INFO --- OrderService : Throwing SQLException in method processOrderWithBackoff()
2024-06-07T10:31:18.571+03:00  INFO --- OrderService : Throwing SQLException in method processOrderWithBackoff()
2024-06-07T10:31:34.577+03:00  INFO --- OrderService : Throwing SQLException in method processOrderWithBackoff()
2024-06-07T10:31:34.580+03:00  INFO --- OrderService : In recoverOrderProcessWithBackoff method
```
### @Retryable Anotasyonu: Parametrelerin Detayli Aciklamasi

#### 1. `value` veya `retryFor`
Bu parametre, hangi istisnalar karsisinda yeniden deneme yapilacagini belirtir. Bir istisna sinifi veya istisna sinifi dizisi kabul eder.

```java
@Retryable(value = { SQLException.class, IOException.class })
```

#### 2. `exclude` veya `noRetryFor`
Bu parametre, yeniden deneme yapilmayacak istisnalari belirtir. Bu istisnalar karsisinda yeniden deneme yapilmaz.

```java
@Retryable(exclude = { IllegalArgumentException.class, NullPointerException.class })
```

#### 3. `maxAttempts`
Yeniden deneme girisimlerinin maksimum sayisini belirtir. Varsayilan deger 3'tur.

```java
@Retryable(maxAttempts = 5)
```

#### 4. `backoff`
Yeniden denemeler arasindaki bekleme suresini yapilandirmak icin kullanilir. `@Backoff` anotasyonu ile birlikte kullanilir.

```java
@Retryable(backoff = @Backoff(delay = 2000, multiplier = 2))
```

`@Backoff` Anotasyonunun Parametreleri:
- **`delay`**: Ilk yeniden deneme oncesinde bekleme suresini (milisaniye cinsinden) belirtir.
- **`multiplier`**: Her basarisiz denemeden sonra bekleme suresinin carpanini belirtir.
- **`maxDelay`**: Bekleme suresinin maksimum degerini belirtir.
- **`random`**: Yeniden denemeler arasinda rastgele bir gecikme ekler.

#### 5. `recover`
Yeniden deneme girisimlerinin hepsi basarisiz olursa cagrilacak metodun adini belirtir. Bu metod, hatalari ele almak icin bir geri kazanim mekanizmasi saglar.

```java
@Retryable(recover = "recoverOrderProcess")
```

#### 6. `label`
Bu parametre, yeniden deneme islemine bir etiket ekler. Bu etiket, ozellikle loglama ve izleme gibi durumlar icin yararlidir.

```java
@Retryable(label = "orderRetry")
```
