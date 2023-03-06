//
//  GameSceneHandler.swift
//  iosApp
//
//  Created by Bradley Phillips on 1/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class GameSceneHandler: ObservableObject
{
    static let shared = GameSceneHandler()
    
    var activeView: ViewType = .EMPTY
    {
        willSet { Task { await MainActor.run { objectWillChange.send() } } }
    }
    
    private (set) var gameKeyboardIsEnabled = false
    
    private (set) var gameBoard: GameSceneBoard? = nil
    {
        willSet { Task { await MainActor.run { objectWillChange.send() } } }
    }
    
    private let viewModel = GameSceneViewModel()
    
    func setUp()
    {
        if activeView == .EMPTY { viewModel.setUp(handler: self) { _ in self.getGameBoard() } }
    }
    
    private func getGameBoard()
    {
        Task
        {
            await MainActor.run
            {
                Task
                {
                    var rowViews = [GameSceneBoard.Row]()
                    let middleRows = try? await viewModel.getGameBoard()
                    for row in middleRows?.rows() ?? []
                    {
                        var rowTiles = [GameSceneBoard.Tile]()
                        for tile in row
                        {
                            rowTiles.append(GameSceneBoard.Tile(letter: String(describing: tile), state: tile.state()))
                        }
                        rowViews.append(GameSceneBoard.Row(tiles: rowTiles))
                    }
                    
                    gameBoard = GameSceneBoard(rows: rowViews)
                }
            }
        }
    }
    
    func gameHeader() -> GameSceneHeader
    {
        GameSceneHeader(title: viewModel.getHeader().title())
    }
    
    func gameKeyboard() -> GameSceneKeyboard
    {
        var keyRows: [[GameSceneKeyboard.Key]] = []
        
        for (rowIndex, row) in viewModel.getGameKeyboard().rows().enumerated()
        {
            keyRows.append([])
            for key in row
            {
                keyRows[rowIndex].append(
                    GameSceneKeyboard.Key(
                        backgroundColor: key.backgroundColor(),
                        letters: key.letters(),
                        resourceId: key.resourceId(),
                        onTap: { if self.gameKeyboardIsEnabled { key.click() { _ in } } }
                    )
                )
            }
        }
        
        return GameSceneKeyboard(rows: keyRows)
    }
    
    func gameAnnouncement() -> GameSceneAnnouncement?
    {
        if let message = viewModel.getAnnouncement().message()
        {
            return GameSceneAnnouncement(text: message)
        }
        else { return nil }
    }
    
    enum ViewType { case EMPTY, GAME }
}

extension GameSceneHandler: GameSceneHandleable
{
    func onAnnouncementShouldHide() { publishGameView(keyboardIsEnabled: true) }
    
    func onAnnouncementShouldShow() { publishGameView(keyboardIsEnabled: true) }
    
    func onGameOver() { publishGameView() }
    
    func onGameStarted() { publishGameView(keyboardIsEnabled: true) }
    
    func onGuessingWord() { publishGameView() }
    
    func onRevealNextTile() { publishGameView(keyboardIsEnabled: true) }
    
    func onRoundCompleted() { publishGameView(keyboardIsEnabled: true) }
    
    func onStartingGame() { publishGameView() }
    
    private func publishGameView(keyboardIsEnabled: Bool = false)
    {
        gameKeyboardIsEnabled = keyboardIsEnabled
        activeView = ViewType.GAME
        getGameBoard()
    }
}
