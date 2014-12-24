package tools.image;

import java.util.ArrayList;

public class MyImageWorkAdapter extends ImageWorker.ImageWorkerAdapter{

    ArrayList<String> urls;

    public MyImageWorkAdapter(ArrayList<String> urls)
    {
        this.urls = urls;
    }

    @Override
    public Object getItem(int num) {
        return this.urls.get(num);
    }

    @Override
    public int getSize() {
        return this.urls.size();
    }
}
