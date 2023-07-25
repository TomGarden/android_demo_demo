package io.github.tomgarden.wallet_connect_demo.utils

import androidx.annotation.DrawableRes
import com.walletconnect.sign.client.Sign


enum class Chains(
    val chainName: String,
    val chainNamespace: String,
    val chainReference: String,
    val color: String,
    val methods: List<String>,
    val events: List<String>,
    val order: Int,
    val chainId: String = "$chainNamespace:$chainReference"
) {

    ETHEREUM_MAIN(
        chainName = "Ethereum",
        chainNamespace = Info.Eth.chain,
        chainReference = "1",
        color = "#617de8",
        methods = Info.Eth.defaultMethods,
        events = Info.Eth.defaultEvents,
        order = 1
    ),

    POLYGON_MATIC(
        chainName = "Polygon Matic",
        chainNamespace = Info.Eth.chain,
        chainReference = "137",
        color = "#8145e4",
        methods = Info.Eth.defaultMethods,
        events = Info.Eth.defaultEvents,
        order = 2
    ),

    ETHEREUM_KOVAN(
        chainName = "Ethereum Kovan",
        chainNamespace = Info.Eth.chain,
        chainReference = "42",
        color = "#617de8",
        methods = Info.Eth.defaultMethods,
        events = Info.Eth.defaultEvents,
        order = 3
    ),

    OPTIMISM_KOVAN(
        chainName = "Optimism Kovan",
        chainNamespace = Info.Eth.chain,
        chainReference = "69",
        color = "#e70000",
        methods = Info.Eth.defaultMethods,
        events = Info.Eth.defaultEvents,
        order = 4
    ),

    POLYGON_MUMBAI(
        chainName = "Polygon Mumbai",
        chainNamespace = Info.Eth.chain,
        chainReference = "80001",
        color = "#8145e4",
        methods = Info.Eth.defaultMethods,
        events = Info.Eth.defaultEvents,
        order = 5
    ),

    ARBITRUM_RINKBY(
        chainName = "Arbitrum Rinkeby",
        chainNamespace = Info.Eth.chain,
        chainReference = "421611",
        color = "#95bbda",
        methods = Info.Eth.defaultMethods,
        events = Info.Eth.defaultEvents,
        order = 6
    ),

    CELO_ALFAJORES(
        chainName = "Celo Alfajores",
        chainNamespace = Info.Eth.chain,
        chainReference = "44787",
        color = "#f9cb5b",
        methods = Info.Eth.defaultMethods,
        events = Info.Eth.defaultEvents,
        order = 7
    ),
    COSMOS(
        chainName = "Cosmos",
        chainNamespace = Info.Cosmos.chain,
        chainReference = "cosmoshub-4",
        color = "#B2B2B2",
        methods = Info.Cosmos.defaultMethods,
        events = Info.Cosmos.defaultEvents,
        order = 7
    ),
    BNB(
        chainName = "BNB Smart Chain",
        chainNamespace = Info.Eth.chain,
        chainReference = "56",
        color = "#F3BA2F",
        methods = Info.Eth.defaultMethods,
        events = Info.Eth.defaultEvents,
        order = 8
    );

    sealed class Info {
        abstract val chain: String
        abstract val defaultEvents: List<String>
        abstract val defaultMethods: List<String>

        object Eth: Info() {
            override val chain = "eip155"
            override val defaultEvents: List<String> = listOf("chainChanged", "accountsChanged")
            override val defaultMethods: List<String> = listOf(
                "eth_sendTransaction",
                "personal_sign",
                "eth_sign",
                "eth_signTypedData"
            )
        }

        object Cosmos: Info() {
            override val chain = "cosmos"
            override val defaultEvents: List<String> = listOf("chainChanged", "accountsChanged")
            override val defaultMethods: List<String> = listOf(
                "cosmos_signDirect",
                "cosmos_signAmino"
            )
        }
    }

    companion object{
        fun getDefNamespace(): Map<String, Sign.Model.Namespace.Proposal> {
            return listOf(Chains.ETHEREUM_MAIN,/* Chains.POLYGON_MUMBAI*/)
                .groupBy { it.chainNamespace }
                .map { (key: String, selectedChains: List<Chains>) ->
                    key to Sign.Model.Namespace.Proposal(
                        chains = selectedChains.map { it.chainId }, //OR uncomment if chainId is an index
                        methods = selectedChains.flatMap { it.methods }.distinct(),
                        events = selectedChains.flatMap { it.events }.distinct()
                    )
                }.toMap()
        }
    }
}