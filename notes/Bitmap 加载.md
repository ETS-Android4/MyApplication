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

```
    /**
     * 对图片进行解码压缩。
     *
     * @param resourceId 所需压缩的图片资源
     * @param reqHeight  所需压缩到的高度
     * @param reqWidth   所需压缩到的宽度
     * @return Bitmap
     */
    private Bitmap decodeBitmap(int resourceId, int reqHeight, int reqWidth) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //inJustDecodeBounds设置为true，解码器将返回一个null的Bitmap，系统将不会为此Bitmap上像素分配内存。
        //只做查询图片宽高用。
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resourceId, options);
        //查询该图片的宽高。
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        // 如果当前图片的高或者宽大于所需的高或宽，
        // 就进行inSampleSize的2倍增加处理，直到图片宽高符合所需要求。
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while ((halfHeight / inSampleSize >= reqHeight)
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        //inSampleSize获取结束后，需要将inJustDecodeBounds置为false。
        options.inJustDecodeBounds = false;
        //返回压缩后的Bitmap。
        return BitmapFactory.decodeResource(getResources(), resourceId, options);
    }
```