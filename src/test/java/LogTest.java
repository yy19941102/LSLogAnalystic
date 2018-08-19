import com.qianfeng.etl.util.LogUtil;

import java.util.Map;

public class LogTest {
    public static void main(String[] args) {
        Map<String, String> info = LogUtil.handleLog("114.61.94.253^A1522211019.456^Ahh^A/BCImg.gif?en=e_pv&p_url=http%3A%2F%2Flocalhost%3A8080%2Fbf_track_jssdk%2Fdemo4.jsp&p_ref=http%3A%2F%2Flocalhost%3A8080%2Fbf_track_jssdk%2Fdemo.jsp&tt=%E6%B5%8B%E8%AF%95%E9%A1%B5%E9%9D%A24&ver=1&pl=java_server&sdk=js&u_ud=27F69684-BBE3-42FA-AA62-71F98E208444&u_mid=Aidon&u_sd=38F66FBB-C6D6-4C1C-8E05-72C31675C00A&c_time=1449917532456&l=zh-CN&b_iev=Mozilla%2F5.0%20(Windows%20NT%206.1%3B%20WOW64)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Chrome%2F46.0.2490.71%20Safari%2F537.36&b_rst=1280*768");
        for (Map.Entry<String, String> en : info.entrySet()) {
            System.out.println(en.getKey() + ":" + en.getValue());
        }
    }
}
