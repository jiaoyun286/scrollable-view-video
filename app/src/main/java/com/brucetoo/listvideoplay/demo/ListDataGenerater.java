package com.brucetoo.listvideoplay.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruce Too
 * On 10/20/16.
 * At 10:13
 */

public class ListDataGenerater {

    static List<VideoModel> datas = new ArrayList<>();

    static {
        VideoModel m1 = new VideoModel();
        m1.coverImage = "https://img-blog.csdnimg.cn/20190301125255914.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTAxMDE5OA==,size_16,color_FFFFFF,t_70";
//        m1.videoUrl = "http://video.pp.cn/fs08/2017/02/09/9/200_b34d50d8820d19961f73f57359e4ca45.mp4";
        m1.videoUrl = "https://media.w3.org/2010/05/sintel/trailer.mp4";

        VideoModel m2 = new VideoModel();
        m2.coverImage = "https://img-blog.csdnimg.cn/20190301125640791.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTAxMDE5OA==,size_16,color_FFFFFF,t_70";
        m2.videoUrl = "http://mirror.aarnet.edu.au/pub/TED-talks/911Mothers_2010W-480p.mp4";

        VideoModel m3 = new VideoModel();
        m3.coverImage = "https://img-blog.csdnimg.cn/20190301125528758.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTAxMDE5OA==,size_16,color_FFFFFF,t_70";
        m3.videoUrl = "https://media.w3.org/2010/05/sintel/trailer.mp4";

        VideoModel m4 = new VideoModel();
        m4.coverImage = "https://s2.ax1x.com/2019/10/17/Kk5u11.md.png";
        m4.videoUrl = "http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4";

        VideoModel m5 = new VideoModel();
        m5.coverImage = "https://s2.ax1x.com/2019/10/15/KC1xOA.md.png";
        m5.videoUrl = "http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4";

        VideoModel m6 = new VideoModel();
        m6.coverImage = "https://s2.ax1x.com/2019/10/15/KC84V1.md.png";
        m6.videoUrl = "http://vfx.mtime.cn/Video/2019/03/17/mp4/190317150237409904.mp4";

        VideoModel m7 = new VideoModel();
        m7.coverImage = "http://android-imgs.25pp.com/fs08/2017/02/23/2/48da103a3a21d8a1dea01570bc35de8e.jpg";
        m7.videoUrl = "http://video.pp.cn/fs08/2017/02/23/10/aa74cfad-fca1-4aa4-9969-4a22d0d2b45b.mp4";

        VideoModel m8 = new VideoModel();
        m8.coverImage = "http://android-imgs.25pp.com/fs08/2017/01/08/3/8a6040d0a4fad07180f8e3762f63a2ee.jpg";
        m8.videoUrl = "http://video.pp.cn/fs08/2017/01/08/3/200_abbb1c85c5c1d9d13cebb33ac7931ea3.mp4";

        VideoModel m9 = new VideoModel();
        m9.coverImage = "http://android-imgs.25pp.com/fs08/2017/01/14/5/8238328e751cabe493ec23f0721ab767.jpg";
        m9.videoUrl = "http://video.pp.cn/fs08/2017/01/14/6/200_95e2c6d6c267df6453c22e23fda0a5a5.mp4";

        VideoModel m10 = new VideoModel();
        m10.coverImage = "http://android-imgs.25pp.com/fs08/2017/04/11/1/cee48982ad11e3d333bfa6efaf72f12c.jpg";
        m10.videoUrl = "http://video.pp.cn/fs08/2017/04/11/7/200_b81b52e4df88bb878248623045d47cca.mp4";

        VideoModel m11 = new VideoModel();
        m11.coverImage = "http://android-imgs.25pp.com/fs08/2017/04/11/5/6d10e5650766c2260e5263d83b1aa2b0.jpg";
        m11.videoUrl = "http://video.pp.cn/fs08/2017/04/11/11/90751092-3f1d-403d-81c0-9cb8f512c9c1.mp4";

        VideoModel m12 = new VideoModel();
        m12.coverImage = "http://android-imgs.25pp.com/fs08/2017/04/11/8/54bdc1f5156cfc63005fd0fecd533897.jpg";
        m12.videoUrl = "http://video.pp.cn/fs08/2017/04/11/6/200_0c869e0dd681b98e459fad414a528005.mp4";

        datas.add(m1);
        datas.add(m2);
        datas.add(m3);
        datas.add(m4);
        datas.add(m5);
        datas.add(m6);
        datas.add(m7);
        datas.add(m8);
        datas.add(m9);
        datas.add(m10);
        datas.add(m11);
        datas.add(m12);
    }

//    public static List<VideoModel> getListData() {
//        return datas;
//    }
}
