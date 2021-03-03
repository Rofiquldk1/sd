// aidlInterface.aidl
package SeparatePk;

// Declare any non-default types here with import statements
import SeparatePk.aidlInterface;
import SeparatePk.IRemoteServiceCallback;

interface aidlInterface {
    void trigger(IRemoteServiceCallback iRemoteServiceCallback);
    void SendSMS(int smsId);
}