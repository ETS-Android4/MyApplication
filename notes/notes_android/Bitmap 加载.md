# Bitmap 加载

Bitmap占用内存 = 横向像素数 x 纵向像素数 x 每个像素占用内存（默认是4）

## Bitmap优化

1. 根据不同的密度的设备将图片资源放置再不同的drawable文件夹中；
2. 利用inSampleSize对图片进行尺寸上的压缩；
3. 利用inPreferredConfig对图片进行质量上的压缩，一般采用RGB_565模式，这个模式下一个像素点占用2bytes；
4. 利用三级缓存，依次从内存缓存（LruCache）、磁盘缓存（DiskLruCache）、网络上获取图片；
5. 使用第三方库Glide、Fresco等。

## 高效加载

1. 将BitmapFactory.Options的 inJustDecodeBounds 设置为true并加载图片
2. 获取图片原始的宽高，对应outWidth和outHeight
3. 计算 inSampleSize
4. inJustDecodeBounds 设置为false，重新加载该图片
