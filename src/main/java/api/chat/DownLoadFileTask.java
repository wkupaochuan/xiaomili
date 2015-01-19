package api.chat;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

public class DownLoadFileTask extends Thread{

    private int threadNum = 3;
    private String fileUrl;
    private String filePath;


    public DownLoadFileTask(String fileUrl, String filePath)
    {
        this.fileUrl = fileUrl;
        this.filePath = filePath;
    }

    /**
     * 执行下载任务
     */
    public void run()
    {
        try {
            URL url = new URL(this.fileUrl);
            URLConnection conn = url.openConnection();
            //防止返回-1
            InputStream in = conn.getInputStream();
            //获取下载文件的总大小
            int fileSize = conn.getContentLength();
            int blockSize = fileSize/this.threadNum;

            File file = new File(this.filePath);
            for(int i = 0; i < this.threadNum; ++i)
            {
                int beginPosition = i * blockSize;
                int endPostion = (i + 1) * blockSize - 1;
                if(endPostion >= fileSize)
                {
                    endPostion = fileSize - 1;
                }
                FileDownloadThread fdt = new FileDownloadThread(url, file, beginPosition, endPostion);
                fdt.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    private class FileDownloadThread extends  Thread{
        private static final int BUFFER_SIZE = 1024;
        private URL fileUrl;
        private File file;
        private int beginPosition;
        private int endPosition;
        private int curPosition;


        public FileDownloadThread(URL url, File file, int beginPosition, int endPosition)
        {
            this.fileUrl = url;
            this.file = file;
            this.beginPosition = beginPosition;
            this.curPosition = beginPosition;
            this.endPosition = endPosition;
        }


        /**
         * 执行下载
         */
        public void run()
        {
            BufferedInputStream bis = null;
            RandomAccessFile fos = null;
            byte[] buf = new byte[FileDownloadThread.BUFFER_SIZE];

            try {
                URLConnection conn = this.fileUrl.openConnection();
                //设置当前线程下载的起止点
                conn.setRequestProperty("Range", "bytes=" + this.beginPosition + "-" + this.endPosition);

                //使用java中的RandomAccessFile 对文件进行随机读写操作
                fos = new RandomAccessFile(file, "rw");
                //设置写文件的起始位置
                fos.seek(this.beginPosition);
                bis = new BufferedInputStream(conn.getInputStream());

                while(this.curPosition < this.endPosition)
                {
                    int len = bis.read(buf, 0 , FileDownloadThread.BUFFER_SIZE);
                    if(len == -1)
                    {
                        break;
                    }
                    fos.write(buf, 0, len);
                    this.curPosition += len;
                }
                bis.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
