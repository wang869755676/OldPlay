      RequestBody body=  RequestBody.create(MediaType.parse("application/json"), "<xml><appid>wx6dd44d2fc12bc9d9</appid><body>苏仰英</body><mch_id>1370856402</mch_id><nonce_str>9abe36658bff8131d5a0923ebc196d0e</nonce_str><notify_url>http://222.75.162.202:20003/index.php/usermoney/weixin_chongzhi_notify_url/</notify_url><out_trade_no>1501661593339720</out_trade_no><spbill_create_ip>127.0.0.1</spbill_create_ip><total_fee>100</total_fee><trade_type>APP</trade_type><sign>9805C89A608C4DCE95324BACE0F2DBDE</sign></xml>");
                HttpManager.getInstance().create(body, "https://api.mch.weixin.qq.com/pay/unifiedorder")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new HttpSubscriber<ResponseBody>(new OnResultCallBack<ResponseBody>() {


                            @Override
                            public void onSuccess(ResponseBody responseBody) {
                                Log.e("==========", "-------------------------------");
                                try {
                                    Log.e("===", responseBody.string() + "----------------");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(int code, String errorMsg) {
                                Log.e("==========", "------------------------------d-"+errorMsg);
                            }

                            @Override
                            public void onSubscribe(Disposable d) {

                            }
                        }));
/*# OldPlay

