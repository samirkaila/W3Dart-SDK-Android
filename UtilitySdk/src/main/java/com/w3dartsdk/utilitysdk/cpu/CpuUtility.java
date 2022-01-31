package com.w3dartsdk.utilitysdk.cpu;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.w3dartsdk.utilitysdk.SDKConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CpuUtility {

    static String tag = CpuUtility.class.getSimpleName();

    ProcessBuilder processBuilder;
    String Holder = "";
    String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
    InputStream inputStream;
    Process process;
    byte[] byteArry;

    String[] abis;

    public CpuUtility(Context context) {

        byteArry = new byte[1024];

  /*      try {
            processBuilder = new ProcessBuilder(DATA);
            process = processBuilder.start();
            inputStream = process.getInputStream();
            while (inputStream.read(byteArry) != -1) {
//                Log.e(tag, "byteArry ---------->: " + new String(byteArry));
                Holder = Holder + new String(byteArry);
            }
            inputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
//        Log.e(tag, "Device Holder: " + Holder);*/

        String result = null;
        CMDExecute cmdexe = new CMDExecute();
        try {
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            result = cmdexe.run(args, "/system/bin/");
//            Log.i("result", "result=" + result);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        unparsed_CPU_INFO = result;

        if (Build.SUPPORTED_ABIS != null) {
            abis = Build.SUPPORTED_ABIS;
        }

      /*
        System.out.println("----------------------------------CPU Utility Start-------------------------------------------------");

        System.out.println("Your cpu model is: " + getCPUName());
        Log.e(tag, "CPU Model: " + getCPUName());
        Log.e(tag, "CPU ABIs Supported: " + Arrays.toString(abis));

        *//*Properties prop = System.getProperties();
        Set<Object> keySet = prop.keySet();
        for (Object obj : keySet) {
            System.out.println("System Property: {"
                    + obj.toString() + ","
                    + System.getProperty(obj.toString()) + "}");
        }*//*


        if (Build.VERSION.SDK_INT >= 17) {
            Log.i(tag, "Number of Cores: " + Runtime.getRuntime().availableProcessors());
        } else {
            Log.i(tag, "Number of Cores: " + getNumCoresLegacy());
        }
        System.out.println("----------------------------------CPU Utility END-------------------------------------------------");
      */

       /* String result = null;
        CMDExecute cmdexe = new CMDExecute();
        try {
            String[] args = { "/system/bin/cat", "/proc/cpuinfo" };
            result = cmdexe.run(args, "/system/bin/");
            Log.i("result", "result=" + result);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    */

    }

    String unparsed_CPU_INFO;
    String cpuName;

    public synchronized String getCPUName() {
        if (cpuName == null) {
            String CPUName = "";

            String[] lines = unparsed_CPU_INFO.split("\n");

            for (int i = 0; i < lines.length; i++) {

                String temp = lines[i];

                if (lines[i].contains("Processor\t:")) {

                    CPUName = lines[i].replace("Processor\t: ", "");
                    break;
                }
            }
            cpuName = CPUName;
            return CPUName;
        } else {
            return cpuName;
        }
    }

    public String getAbis() {
        return Arrays.toString(abis);
    }

    public String getDetail() {
        return "CPU { \"" + SDKConstants.KEY_CPU + "\" : \"" + getCPUName() + "\"" +
                " , " + " \"" + SDKConstants.KEY_CPU_INSTRUCTION_SET + "\" : \"" + getAbis() + "\"" +
                "}";
    }

    public static Map<String, String> getCPUInfo() throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("/proc/cpuinfo"));
        String str;
        Map<String, String> output = new HashMap<>();

        while ((str = br.readLine()) != null) {
            String[] data = str.split(":");
            if (data.length > 1) {
                String key = data[0].trim().replace(" ", "_");
                if (key.equals("model_name")) key = "cpu_model";
                output.put(key, data[1].trim());
            }
        }
        br.close();
        return output;

    }

    class CMDExecute {

        public synchronized String run(String[] cmd, String workdirectory)
                throws IOException {
            String result = "";

            try {
                ProcessBuilder builder = new ProcessBuilder(cmd);
                // set working directory
                if (workdirectory != null)
                    builder.directory(new File(workdirectory));
                builder.redirectErrorStream(true);
                Process process = builder.start();
                InputStream in = process.getInputStream();
                byte[] re = new byte[1024];
                while (in.read(re) != -1) {
//                    System.out.println(new String(re));
                    result = result + new String(re);
                }
                in.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return result;
        }

    }


    /**
     * @return integer Array with 4 elements: user, system, idle and other cpu
     * usage in percentage.
     */
    private int[] getCpuUsageStatistic() {

        String tempString = executeTop();

        tempString = tempString.replaceAll(",", "");
        tempString = tempString.replaceAll("User", "");
        tempString = tempString.replaceAll("System", "");
        tempString = tempString.replaceAll("IOW", "");
        tempString = tempString.replaceAll("IRQ", "");
        tempString = tempString.replaceAll("%", "");
        for (int i = 0; i < 10; i++) {
            tempString = tempString.replaceAll("  ", " ");
        }
        tempString = tempString.trim();
        String[] myString = tempString.split(" ");
        int[] cpuUsageAsInt = new int[myString.length];
        for (int i = 0; i < myString.length; i++) {
            myString[i] = myString[i].trim();
//            cpuUsageAsInt[i] = Integer.parseInt(myString[i]);
            Log.i("executeTop", "cpuUsageAsInt: " + myString[i]);
        }
        return cpuUsageAsInt;
    }

    private String executeTop() {
        Process p = null;
        BufferedReader in = null;
        String returnString = null;
        try {
            p = Runtime.getRuntime().exec("top -n 1");
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while (returnString == null || returnString.contentEquals("")) {
                returnString = in.readLine();
            }
        } catch (IOException e) {
            Log.e("executeTop", "error in getting first line of top");
            e.printStackTrace();
        } finally {
            try {
                in.close();
                p.destroy();
            } catch (IOException e) {
                Log.e("executeTop", "error in closing and destroying top process");
                e.printStackTrace();
            }
        }
        return returnString;
    }


    /*public class MainActivity extends AppCompatActivity {
        TextView textView;
        ProcessBuilder processBuilder;
        String Holder = "";
        String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
        InputStream inputStream;
        Process process;
        byte[] byteArry;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            textView = (TextView) findViewById(R.id.textView);
            byteArry = new byte[1024];
            try {
                processBuilder = new ProcessBuilder(DATA);
                process = processBuilder.start();
                inputStream = process.getInputStream();
                while (inputStream.read(byteArry) != -1) {
                    Holder = Holder + new String(byteArry);
                }
                inputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            textView.setText(Holder);
        }
    }*/


    private int getNumCoresLegacy() {

        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                if (Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        int core = 1;
        try {
            core = new File("/sys/devices/system/cpu/").listFiles(new CpuFilter()).length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return core;
    }


}