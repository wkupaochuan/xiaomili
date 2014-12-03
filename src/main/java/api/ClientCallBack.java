package api;


public interface ClientCallBack {
    abstract void onSuccess(Object data);
    abstract void onFailure(String message);
}
