package es.aroldan.rickandmorty.presentation.screen.splash

import es.aroldan.rickandmorty.presentation.model.Event
import es.aroldan.rickandmorty.presentation.model.ScreenNavigation
import es.aroldan.rickandmorty.presentation.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    private var dispatcher: TestDispatcher = StandardTestDispatcher()

    private lateinit var splashViewModelTested: SplashViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        splashViewModelTested = SplashViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when animation finished then send navigate event`() = runTest {
        splashViewModelTested.animationFinished()

        Assert.assertEquals(
            Event.Navigate(ScreenNavigation.Navigate(Constant.Deeplink.Path.CHARACTERS, true)),
            splashViewModelTested.events.first()
        )
    }
}