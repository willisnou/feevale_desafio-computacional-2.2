package br.feevale.environmentalthreatsfirebase;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Utils {

    public static boolean validateDate(String date){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try{
            format.parse(date);
            return true;
        }catch (ParseException e){
            return false;
        }
    }

    public static final String THREATS_KEY = "environmentalthreats";

    public static class InternalResponse{
        public final static int UPDATE_ON_INSERT = 1001;
        public final static int UPDATE_ON_UPDATE = 1002;
        public final static int CAMERA_CALL = 1003;
    }

    public static class Mask {
        public static TextWatcher insert(final String mask, final EditText et) {
            return new TextWatcher() {
                boolean isUpdating;
                String oldTxt = "";
                public void onTextChanged(
                        CharSequence s, int start, int before,int count) {
                    String str = Mask.unmask(s.toString());
                    String maskCurrent = "";
                    if (isUpdating) {
                        oldTxt = str;
                        isUpdating = false;
                        return;
                    }
                    int i = 0;
                    for (char m : mask.toCharArray()) {
                        if (m != '#' && str.length() > oldTxt.length()) {
                            maskCurrent += m;
                            continue;
                        }
                        try {
                            maskCurrent += str.charAt(i);
                        } catch (Exception e) {
                            break;
                        }
                        i++;
                    }
                    isUpdating = true;
                    et.setText(maskCurrent);
                    et.setSelection(maskCurrent.length());
                }
                public void beforeTextChanged(
                        CharSequence s, int start, int count, int after) {}
                public void afterTextChanged(Editable s) {}
            };
        }
        private static String unmask(String s) {
            return s.replaceAll("[.]", "").replaceAll("[-]", "")
                    .replaceAll("[/]", "").replaceAll("[(]", "")
                    .replaceAll("[)]", "");
        }
    }
}
