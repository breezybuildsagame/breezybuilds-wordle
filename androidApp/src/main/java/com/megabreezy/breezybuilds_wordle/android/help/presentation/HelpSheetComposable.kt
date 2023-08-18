package com.megabreezy.breezybuilds_wordle.android.help.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.ui.image.ImageComponent
import com.megabreezy.breezybuilds_wordle.android.core.ui.tile.CoreTile
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions
import com.megabreezy.breezybuilds_wordle.android.util.theme.ThemeFonts
import com.megabreezy.breezybuilds_wordle.android.util.theme.dpToSp
import com.megabreezy.breezybuilds_wordle.feature.help.domain.model.HelpSheet

object HelpSheetComposable
{
    @Composable
    fun Component(options: ComponentOptions = ComponentOptions())
    {
        val titleTextStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = ThemeFonts.roboto,
            fontSize = dpToSp(dp = LocalSceneDimensions.current.height.times(20 / Scene.idealFrame().height)),
            fontWeight = FontWeight.Black,
            lineHeight = dpToSp(dp = LocalSceneDimensions.current.height.times(50 / Scene.idealFrame().height)),
            textAlign = TextAlign.Center
        )
        val footerTextStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = ThemeFonts.roboto,
            fontSize = dpToSp(dp = LocalSceneDimensions.current.height.times(15 / Scene.idealFrame().height)),
            fontWeight = FontWeight.Black
        )

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "${TagName.CONTENT}" }
        )
        {
            Column(
                verticalArrangement = Arrangement.spacedBy(LocalSceneDimensions.current.height.times(20 / Scene.idealFrame().height)),
                modifier = Modifier
                    .width(LocalSceneDimensions.current.width.times(350 / Scene.idealFrame().width))
            )
            {
                Text(
                    text = options.title,
                    modifier = Modifier
                        .padding(horizontal = LocalSceneDimensions.current.height.times(25 / Scene.idealFrame().height))
                        .width(width = LocalSceneDimensions.current.height.times(300 / Scene.idealFrame().height))
                        .aspectRatio(ratio = 300f / 50)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .semantics { helpSheetComposableTitleTextStyle = titleTextStyle },
                    style = titleTextStyle
                )

                options.instructions.forEach { it() }

                Divider(
                    color = MaterialTheme.colorScheme.error,
                    thickness = LocalSceneDimensions.current.height.times(1 / Scene.idealFrame().height)
                )

                options.examples.forEach { it() }

                Divider(
                    color = MaterialTheme.colorScheme.error,
                    thickness = LocalSceneDimensions.current.height.times(1 / Scene.idealFrame().height)
                )

                Text(
                    text = options.footer,
                    modifier = Modifier
                        .semantics { helpSheetComposableFooterTextStyle = footerTextStyle },
                    style = footerTextStyle
                )
            }

            options.closeButton?.let()
            {
                Column(modifier = Modifier.fillMaxWidth())
                {
                    it()

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }

    object Tile
    {
        @Composable
        fun Component(options: ComponentOptions = ComponentOptions())
        {
            CoreTile.Component(
                options = CoreTile.ComponentOptions(
                    letter = options.letter,
                    modifier = Modifier
                        .semantics { contentDescription = "${TagName.TILE}" },
                    state = options.state.name,
                    tileSize = 50f
                )
            )
        }

        data class ComponentOptions(
            val state: HelpSheet.Tile.State = HelpSheet.Tile.State.HIDDEN,
            val letter: String = ""
        )
    }

    object Example
    {
        @Composable
        fun Component(options: ComponentOptions = ComponentOptions())
        {
            val descriptionTextStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = ThemeFonts.roboto,
                fontSize = dpToSp(dp = LocalSceneDimensions.current.height.times(15 / Scene.idealFrame().height)),
                fontWeight = FontWeight.Normal
            )

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .semantics { contentDescription = "${TagName.EXAMPLE}" },
                verticalArrangement = Arrangement.spacedBy(
                    LocalSceneDimensions.current.height.times(15 / Scene.idealFrame().height))
            )
            {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LocalSceneDimensions.current.width.times(6 / Scene.idealFrame().width)),
                    modifier = Modifier
                        .semantics { contentDescription = "ROW" }
                )
                {
                    options.tiles.forEach { it() }
                }

                Row(
                    modifier = Modifier
                        .semantics { contentDescription = "ROW" }
                )
                {
                    Text(
                        text = options.description,
                        modifier = Modifier
                            .semantics { helpSheetExampleDescriptionTextStyle = descriptionTextStyle },
                        style = descriptionTextStyle
                    )

                    Spacer(modifier = Modifier
                        .weight(1f)
                        .semantics { contentDescription = "spacer" })
                }
            }
        }

        data class ComponentOptions(
            val tiles: List<@Composable () -> Unit> = listOf(),
            val description: String = ""
        )

        val DescriptionTextStyleKey = SemanticsPropertyKey<TextStyle>("DescriptionTextStyle")

        private var SemanticsPropertyReceiver.helpSheetExampleDescriptionTextStyle by DescriptionTextStyleKey
    }

    object Instruction
    {
        @Composable
        fun Component(options: ComponentOptions = ComponentOptions())
        {
            val textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = ThemeFonts.roboto,
                fontSize = dpToSp(dp = LocalSceneDimensions.current.height.times(15 / Scene.idealFrame().height)),
                fontWeight = FontWeight.Normal
            )

            Row(
                modifier = Modifier
                    .semantics { contentDescription = "${TagName.INSTRUCTION}" }
            )
            {
                Text(
                    text = options.instruction,
                    modifier = Modifier
                        .semantics { helpSheetExampleInstructionTextStyle = textStyle },
                    style = textStyle
                )

                Spacer(modifier = Modifier
                    .weight(1f)
                    .semantics { contentDescription = "spacer" })
            }
        }

        data class ComponentOptions(val instruction: String = "")

        val TextStyleKey = SemanticsPropertyKey<TextStyle>("TextStyle")

        private var SemanticsPropertyReceiver.helpSheetExampleInstructionTextStyle by TextStyleKey
    }

    object CloseButton: ImageComponent()
    {
        @Composable
        fun Component(options: ComponentOptions = ComponentOptions())
        {
            Row(
                modifier = Modifier
                    .semantics { contentDescription = "${TagName.CLOSE_BUTTON}" }
            )
            {
                Spacer(modifier = Modifier
                    .weight(1f)
                    .semantics { contentDescription = "spacer" })

                Image(
                    painter = painterResource(id = getDrawableResourceIdFromImageName(name = "core_image_close_icon")),
                    contentDescription = "core_image_close_icon",
                    modifier = Modifier
                        .padding(top = LocalSceneDimensions.current.height.times(10 / Scene.idealFrame().height))
                        .padding(end = LocalSceneDimensions.current.width.times(25 / Scene.idealFrame().width))
                        .height(height = LocalSceneDimensions.current.height.times(25 / Scene.idealFrame().height))
                        .clickable { options.onClick() },
                    contentScale = ContentScale.Fit
                )
            }
        }

        data class ComponentOptions(val onClick: () -> Unit = { })
    }

    data class ComponentOptions(
        val title: String = "",
        val instructions: List<@Composable () -> Unit> = listOf(),
        val examples: List<@Composable () -> Unit> = listOf(),
        val footer: String = "",
        val closeButton: (@Composable () -> Unit)? = null
    )

    enum class TagName(private val id: String)
    {
        CONTENT(id = "help_sheet_content"),
        TILE(id = "help_sheet_tile"),
        EXAMPLE(id = "help_sheet_example"),
        INSTRUCTION(id = "help_sheet_instruction"),
        CLOSE_BUTTON(id = "help_sheet_close_button");

        override fun toString(): String = this.id
    }

    val TitleTextStyleKey = SemanticsPropertyKey<TextStyle>("TitleTextStyle")
    val FooterTextStyleKey = SemanticsPropertyKey<TextStyle>("FooterTextStyle")

    private var SemanticsPropertyReceiver.helpSheetComposableTitleTextStyle by TitleTextStyleKey
    private var SemanticsPropertyReceiver.helpSheetComposableFooterTextStyle by FooterTextStyleKey
}