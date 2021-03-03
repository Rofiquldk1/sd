// IRemoteServiceCallback.aidl
package SeparatePk;

// Declare any non-default types here with import statements
import SeparatePk.aidlInterface;

interface IRemoteServiceCallback {
    void feedBack(String msg);
    void sentStatus(String message);
}