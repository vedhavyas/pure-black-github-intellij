package dev.vedhavyas.pureblackgithub

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 * Persistent app-level state remembering whether the first-run notification
 * has been shown/acted on. Survives restarts. One flag, one storage file.
 */
@Service(Service.Level.APP)
@State(
    name = "PureBlackGithubFirstRun",
    storages = [Storage("pure-black-github-firstrun.xml")],
)
class FirstRunService : PersistentStateComponent<FirstRunService.MyState> {
    data class MyState(var shown: Boolean = false)

    private var myState = MyState()

    override fun getState(): MyState = myState

    override fun loadState(state: MyState) {
        myState = state
    }

    var shown: Boolean
        get() = myState.shown
        set(value) {
            myState.shown = value
        }
}
