package tr.xip.wanikani.models;

public class Request<T> {
    public User user_information;
    public T requested_information;
    public Error error;
}