package com.quickbite.connector

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align

/**
 * Created by Paha on 1/23/2016.
 */
class GUIManager {

    /* Main Menu GUI Stuff */
    lateinit private var start: TextButton
    lateinit private var quit:TextButton
    lateinit private var colorSame:TextButton
    lateinit private var colorRandom:TextButton
    lateinit private var matchShape:TextButton
    lateinit private var matchColor:TextButton
    lateinit private var modePractice:TextButton
    lateinit private var modeBest:TextButton
    lateinit private var modeTimed:TextButton
    lateinit private var startGame:TextButton
    lateinit private var threeShapes: TextButton
    lateinit private var fourShapes:TextButton
    lateinit private var fiveShapes:TextButton
    lateinit private var sixShapes:TextButton

    private var table = Table()
    lateinit private var gameOverImage: Image
    lateinit private var roundLabel: Label
    lateinit private var avgLabel:Label
    lateinit private var colorTypeLabel:Label
    lateinit private var matchTypeLabel:Label
    lateinit private var gameTypeLabel:Label
    lateinit private var timerLabel:Label
    lateinit private var restartButton: TextButton
    lateinit private var mainMenuButton:TextButton
    lateinit private var backButton: ImageButton

    private fun makeGUI(game:Game, gameScreen : GameScreen) {
        this.table.setFillParent(true)
        Game.stage.addActor(this.table)

        val arrow = TextureRegion(Game.easyAssetManager.get<Texture>("leftArrow", Texture::class.java))

        val imageButtonStyle = ImageButton.ImageButtonStyle()
        imageButtonStyle.up = TextureRegionDrawable(Game.defaultButtonUp)
        imageButtonStyle.down = TextureRegionDrawable(Game.defaultButtonDown)
        imageButtonStyle.imageUp = TextureRegionDrawable(arrow)
        imageButtonStyle.imageDown = TextureRegionDrawable(arrow)

        this.backButton = ImageButton(imageButtonStyle)
        this.backButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                Game.stage.clear()
                game.setScreen(MainMenu(game))
            }
        })
        this.backButton.setSize(64f, 32f)
        this.backButton.setPosition((Gdx.graphics.width / 2 - 32).toFloat(), (Gdx.graphics.height - 32).toFloat())

        val style = TextButton.TextButtonStyle()
        style.up = TextureRegionDrawable(TextureRegion(Game.easyAssetManager.get<Texture>("defaultButton_normal", Texture::class.java)))
        style.down = TextureRegionDrawable(TextureRegion(Game.easyAssetManager.get<Texture>("defaultButton_down", Texture::class.java)))
        style.font = Game.defaultFont

        /* The restart and main menu button for when the game ends */

        this.restartButton = TextButton("Restart", style)
        this.mainMenuButton = TextButton("Main Menu", style)

        this.restartButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                gameScreen.restart()
            }
        })
        this.mainMenuButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                Game.stage.clear()
                game.setScreen(MainMenu(game))
            }
        })

        /* The labels for information about the game*/

        var colorType = "Colors: Normal"
        var matchType = "Matching: Shapes"
        var gameType = "Practice"

        if (GameSettings.colorType == GameSettings.ColorType.Random)
            colorType = "Colors: Random"

        if (GameSettings.matchType == GameSettings.MatchType.Color)
            matchType = "Matching: Colors"

        if (GameSettings.gameType == GameSettings.GameType.Fastest)
            gameType = "Mode: Fastest"
        else if (GameSettings.gameType == GameSettings.GameType.Timed)
            gameType = "Mode; TimeAttack"

        var labelStyle = Label.LabelStyle(Game.defaultFont, Color.WHITE)
        val titleLabelStyle = Label.LabelStyle(Game.defaultLargeFont, Color.WHITE)

        this.colorTypeLabel = Label(colorType, labelStyle)
        this.matchTypeLabel = Label(matchType, labelStyle)
        this.gameTypeLabel = Label(gameType, labelStyle)

        val labelTable = Table()
        labelTable.left().top()
        labelTable.setFillParent(true)
        Game.stage.addActor(labelTable)

        labelTable.add<Label>(colorTypeLabel).left()
        labelTable.row()
        labelTable.add<Label>(matchTypeLabel).left()
        labelTable.row()
        labelTable.add<Label>(gameTypeLabel).left()

        if (GameSettings.gameType == GameSettings.GameType.Fastest) {
            val otherTable = Table()
            otherTable.setFillParent(true)
            otherTable.right().top()
            labelStyle = Label.LabelStyle(Game.defaultFont, Color.WHITE)

            this.avgLabel = Label("avg-time: 0", labelStyle)
            this.avgLabel.setAlignment(Align.center)
            this.avgLabel.setSize(100f, 50f)
            otherTable.add<Label>(avgLabel)
            otherTable.row()

            this.roundLabel = Label("0 / 0 / 0", labelStyle)
            this.roundLabel.setAlignment(Align.center)
            this.roundLabel.setSize(100f, 50f)
            otherTable.add<Label>(roundLabel)

            Game.stage.addActor(otherTable)
        }

        if (GameSettings.gameType == GameSettings.GameType.Timed) {
            this.timerLabel = Label("", titleLabelStyle)
            this.timerLabel.setAlignment(Align.center)
            this.timerLabel.setSize(100f, 50f)
            this.timerLabel.setPosition((Gdx.graphics.width / 2 - 50).toFloat(), (Gdx.graphics.height - 75).toFloat())
            Game.stage.addActor(this.timerLabel)
        }

        Game.stage.addActor(this.backButton)
    }

}