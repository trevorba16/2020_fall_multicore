package q5.Tournament;

public interface Lock {
    public void lock(int pid);
    public void unlock(int pid);
}
