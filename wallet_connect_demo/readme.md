
```
runtimeOnly 'com.walletconnect:sign:2.17.0'
```
- 它要求 `kotlin_version` 值为 1.7.20




需要解决的问题
- 连接一次后 , 就不能再连接了
  - 涉及到整体连接流程应该是没有闭环 , 所以无法发起连接
  - 就 Android 而言 , 可以通过 `SignClient.getListOfActiveSessions()` 获取当前可用的 Session , 其中包含钱包地址
  - pairing 过期意味着什么
    - 就我们作的测试来看 , 针对 metaMask 不论我们设置何种过期时间 , 都没有生效 , 最终 `SignClient.getListOfActiveSessions()`   
      获取的过期时间都是 自创建时刻 起 7 日过期 ; 
      - 我们试过的方式 对以下 三个 expiry 进行修改都没有任何效果 :
         ```json5
         {
           "namespaces": {"...": "..."},
           "optionalNamespaces": {"...": "..."},
           "pairing": {
             "expiry": 1690341278,
             "...": "...",
           },
           "properties": {
             "...": "...",
             "sessionExpiry": "1690341278",
             "expiry": "1690341278"
           }
         }
         ```
    - 需要作出测试






connect param 中 methods 含义 了解 : https://docs.walletconnect.com/2.0/advanced/rpc-reference/ethereum-rpc#personal_sign




几分钟内多次 connect 已经存在的 pairing 会报错
```
错误描述信息也是一样看不懂: https://github.com/ChainAgnostic/CAIPs/blob/master/CAIPs/caip-25.md
┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────
│ Thread: DefaultDispatcher-worker-6
├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
│ WalletConnectModal$setDelegate$signDelegate$1.onSessionRejected  (WalletConnectModal.kt:59)
│    WalletConnectModalDelegate.onSessionRejected  (WalletConnectModalDelegate.kt:29)
├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
│ onSessionRejected : {"reason":"User rejected methods. : code: 5002","topic":"d8a8ba0d541fffb0875f3f592a2f2ecc064e7d2d6cb69a5b457dd4a418eaa1ad"}
└────────────────────────────────────────────────────────────────────────────────────────────────────────────────
```


