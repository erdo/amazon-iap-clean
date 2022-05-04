package com.example.amzn.domain.iap

import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.observer.ObservableImp
import kotlinx.serialization.Serializable

interface UserOwned {
    fun clearOranges()
    fun setNewUser(amazonUserId: String, amazonMarketplaceId: String)
}

/**
 * We probably won't need this - I guess we will get that info from atom?
 *
 * Things that we bought already and are available. Not backed by a db or anything, so if we
 * change the user: all the things that the previous user bought are deleted locally
 */
class BoughtItems : Observable by ObservableImp(), UserOwned {

    var state = State()
        private set

    fun addNewOrange() {

        Fore.getLogger().i("addNewOrange()")

        state = state.copy(
            numberOfOrangesAvailable = state.numberOfOrangesAvailable + 1
        )
        notifyObservers()
    }

    fun eatOrange() {

        Fore.getLogger().i("eatOrange() (canEatOrange:${canEatOrange()})")

        if (canEatOrange()) {
            state = state.copy(
                numberOfOrangesAvailable = state.numberOfOrangesAvailable - 1,
                numberOfOrangesEaten = state.numberOfOrangesEaten + 1
            )
            notifyObservers()
        }
    }

    fun canEatOrange() = state.canEatOrange()

    /**
     * if the user is different, this forgets the items bought by any previous user
     */
    override fun setNewUser(amazonUserId: String, amazonMarketplaceId: String) {

        Fore.getLogger()
            .i("setNewUser() user:${state.amazonUserId} market:${state.amazonMarketplaceId}")

        if (state.amazonUserId != amazonUserId || state.amazonMarketplaceId != amazonMarketplaceId) {
            Fore.getLogger().i("     ----> user is different, clearing all bought items")
            state = State(
                amazonUserId = amazonUserId,
                amazonMarketplaceId = amazonMarketplaceId,
            )
            notifyObservers()
        }
    }

    override fun clearOranges() {

        Fore.getLogger().i("clearOranges()")

        state = state.copy(
            numberOfOrangesAvailable = 0,
            numberOfOrangesEaten = 0,
        )
        notifyObservers()
    }
}

@Serializable
data class State(
    val numberOfOrangesAvailable: Int = 0,
    val numberOfOrangesEaten: Int = 0,
    val amazonUserId: String = "",
    val amazonMarketplaceId: String = ""
) {
    fun canEatOrange(): Boolean = numberOfOrangesAvailable > 0
}
