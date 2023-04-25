package com.megabreezy.breezybuilds_wordle.android.core.ui.app_modal

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions
import com.megabreezy.breezybuilds_wordle.android.stats.presentation.component.StatsGuessDistribution
import com.megabreezy.breezybuilds_wordle.android.stats.presentation.component.StatsModalContent
import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.AppModalHelper
import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.AppModalRepresentable
import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.AppModalViewHandleable
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.StatsModal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AppModalViewHandler(
    private val appModal: AppModalRepresentable = AppModalHelper().appModal(),
    private val scope: CoroutineScope? = null
): AppModalViewHandleable
{
    var appModalIsShowing by mutableStateOf(false)

    fun setUp() { appModal.setHandler(newHandler = this) }

    override fun onModalShouldShow(animationDuration: Long) { appModalIsShowing = true }

    override fun onModalShouldHide(animationDuration: Long) { appModalIsShowing = false }

    @Composable
    fun ModalContent()
    {
        when (appModal.content())
        {
            is StatsModal -> {
                val statsModal = (appModal.content() as StatsModal)
                val offset = remember { mutableStateOf(0f) }

                StatsModalContent.Component(
                    options = StatsModalContent.ComponentOptions(
                        statsRow = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(LocalSceneDimensions.current.width.times(5 / Scene.idealFrame().width)),
                                modifier = Modifier
                                    .scrollable(
                                        orientation = Orientation.Horizontal,
                                        state = rememberScrollableState { delta ->
                                            offset.value = offset.value + delta
                                            delta
                                        }
                                    )
                                    .semantics { contentDescription = "${StatsModalContent.TagName.ROW}" },
                                verticalAlignment = Alignment.Top
                            )
                            {
                                statsModal.stats().map()
                                { stat ->
                                    StatsModalContent.Stat.Component(
                                        options = StatsModalContent.Stat.ComponentOptions(
                                            modifier = Modifier.weight(1f),
                                            headline = stat.headline(),
                                            description = stat.description()
                                        )
                                    )
                                }
                            }
                        },
                        guessDistribution = {
                            StatsGuessDistribution.Component(
                                options = StatsGuessDistribution.ComponentOptions(
                                    title = statsModal.guessDistribution().title(),
                                    rows = statsModal.guessDistribution().rows().map
                                    { sharedRow ->
                                        {
                                            StatsGuessDistribution.GraphRow.Component(
                                                options = StatsGuessDistribution.GraphRow.ComponentOptions(
                                                    round = "${sharedRow.round()}",
                                                    correctGuessCount = "${sharedRow.correctGuessesCount()}"
                                                )
                                            )
                                        }
                                    }
                                )
                            )
                        },
                        playAgainButton = {
                            StatsModalContent.PlayAgainButton.Component(
                                options = StatsModalContent.PlayAgainButton.ComponentOptions(
                                    label = statsModal.playAgainButton()?.label(),
                                    onClick = { scope?.launch { statsModal.playAgainButton()?.click() } }
                                )
                            )
                        }
                    )
                )
            }
            else -> println("failed to display content")
        }
    }
}

@Composable
fun rememberAppModalViewHandler(
    appModal: AppModalRepresentable = AppModalHelper().appModal(),
    scope: CoroutineScope? = null
) = remember { AppModalViewHandler(appModal = appModal, scope = scope) }