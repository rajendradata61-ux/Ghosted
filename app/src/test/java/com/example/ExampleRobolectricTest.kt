package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.game.GameViewModel
import com.example.model.GamePhase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Caught in 4K", appName)
    }

    @Test
    fun `full prototype game loop flow`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val vm = GameViewModel(app)

        // 1. Initial State should be HOME
        assertEquals(GamePhase.HOME, vm.uiState.value.phase)

        // 2. Create Room -> Phase becomes LOBBY
        vm.createRoom()
        assertEquals(GamePhase.LOBBY, vm.uiState.value.phase)
        assertTrue(vm.uiState.value.players.isNotEmpty())
        assertTrue(vm.uiState.value.roomCode.isNotEmpty())

        // 3. Start Mini-Game -> Phase becomes GAMEPLAY
        vm.startMiniGame()
        assertEquals(GamePhase.GAMEPLAY, vm.uiState.value.phase)
        assertEquals(10, vm.uiState.value.timerSecondsRemaining)
        assertEquals(3, vm.callOptions.size)

        // 4. Pick Safe Excuse -> Phase becomes RESULT with Victory
        val safeOption = vm.callOptions.first { it.isSuccess }
        vm.selectExcuse(safeOption)
        assertEquals(GamePhase.RESULT, vm.uiState.value.phase)
        assertNotNull(vm.uiState.value.resultData)
        assertTrue(vm.uiState.value.resultData!!.isWin)

        // 5. Play Again -> Resets back to GAMEPLAY
        vm.playAgain()
        assertEquals(GamePhase.GAMEPLAY, vm.uiState.value.phase)
    }
}
