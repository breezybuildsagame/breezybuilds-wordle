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
    
    @Published var activeView: ViewType = .EMPTY
    
    private (set) var gameKeyboardIsEnabled = false
    
    private let viewModel = GameSceneViewModel()
    
    
    func setUp()
    {
        if activeView == .EMPTY { viewModel.setUp(handler: self) }
    }
    
    func gameHeader() -> GameSceneHeader
    {
        GameSceneHeader(title: viewModel.getHeader().title())
    }
    
    func gameBoard() -> GameSceneBoard
    {
        GameSceneBoard(rows: viewModel.getGameBoard().rows())
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
                        onTap: { if self.gameKeyboardIsEnabled { key.click() } }
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
    }
}
