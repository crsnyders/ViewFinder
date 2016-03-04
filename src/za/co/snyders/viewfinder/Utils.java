package za.co.snyders.viewfinder;
import java.io.*;
import android.util.*;

public class Utils{
	
	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(obj);
		return out.toByteArray();
	}
	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return is.readObject();
	}
	
	public static int getResIndex(String resString){
		return Integer.parseInt(resString.split(" ")[0]);
	}
	public static String unitChange(double nWidth, double nHeight) {
        String unit = "";
        Log.v("UTILS", "W=" + nWidth + ", H=" + nHeight);
        if ((nWidth == 4680.0d && nHeight == 3456.0d) || (nWidth == 3456.0d && nHeight == 4680.0d)) {
            return "16 M";
        }
        if ((nWidth == 4680.0d && nHeight == 3072.0d) || (nWidth == 3072.0d && nHeight == 4680.0d)) {
            return "14 MP";
        }
        if ((nWidth == 4320.0d && nHeight == 3240.0d) || (nWidth == 3240.0d && nHeight == 4320.0d)) {
            return "14 M";
        }
        if ((nWidth == 4608.0d && nHeight == 2592.0d) || (nWidth == 2592.0d && nHeight == 4608.0d)) {
            return "12 MW";
        }
        if ((nWidth == 4320.0d && nHeight == 2880.0d) || (nWidth == 2880.0d && nHeight == 4320.0d)) {
            return "12 MP";
        }
        if ((nWidth == 4320.0d && nHeight == 2432.0d) || (nWidth == 2432.0d && nHeight == 4320.0d)) {
            return "10 MW";
        }
        if ((nWidth == 3648.0d && nHeight == 2736.0d) || (nWidth == 2736.0d && nHeight == 3648.0d)) {
            return "10 M";
        }
        if ((nWidth == 2832.0d && nHeight == 2832.0d) || (nWidth == 2832.0d && nHeight == 2832.0d)) {
            return "8 M";
        }
        if ((nWidth == 2592.0d && nHeight == 1944.0d) || (nWidth == 1944.0d && nHeight == 2592.0d)) {
            return "5 M";
        }
        if ((nWidth == 1984.0d && nHeight == 1488.0d) || (nWidth == 1488.0d && nHeight == 1984.0d)) {
            return "3 M";
        }
        if ((nWidth == 1920.0d && nHeight == 1080.0d) || (nWidth == 1080.0d && nHeight == 1920.0d)) {
            return "2 MW";
        }
        if ((nWidth == 1024.0d && nHeight == 768.0d) || (nWidth == 768.0d && nHeight == 1024.0d)) {
            return "1 M";
        }
        return "10 M";
    }
}
