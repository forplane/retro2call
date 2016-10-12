# retro2call-master

16-10-11
更改LoadOperate中isHaveHead的默认
16-10-12
        eFailure(msg);
        改成
        boolean networkAvailable = LoadOperate.isNetworkAvailable(mContext);
        String msg = networkAvailable?t.getMessage():"请连接网络";
        eFailure(msg);
