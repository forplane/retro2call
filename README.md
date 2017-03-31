<<<<<<< HEAD
# retro2call-master

16-10-11
更改LoadOperate中isHaveHead的默认
16-10-12
        eFailure(msg);
        改成
        boolean networkAvailable = LoadOperate.isNetworkAvailable(mContext);
        String msg = networkAvailable?t.getMessage():"请连接网络";
        eFailure(msg);
16-10-14
        public LoadOperate build() {
            ViewGroup parent = (ViewGroup) params.view.getParent();
            LoadOperate loadTool;
            if (parent == null) {
                loadTool = new LoadOperate(params);
            } else {
                Object tag = parent.getTag();
                if (tag != null && tag instanceof LoadOperate) {
                    loadTool = (LoadOperate) tag;
                } else {
                    loadTool = new LoadOperate(params);
                }
            }
            return loadTool;
        }
        判断是否已经有添加过load，预防重复添加
=======
# retro2call
>>>>>>> 006671da1aacf2ea8a2a927866cced13c174feb0
