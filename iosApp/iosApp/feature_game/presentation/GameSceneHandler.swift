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
    
    private let viewModel = GameSceneViewModel()
    
    @Published var activeView: ViewType = .EMPTY
    
    func setUp()
    {
        if activeView == .EMPTY { viewModel.setUp(handler: self) }
    }
    
    func gameHeader() -> GameSceneHeader { GameSceneHeader(title: "Test") }
    
    enum ViewType { case EMPTY, GAME }
}

extension GameSceneHandler: GameSceneHandleable
{
    func onGameOver() { }
    
    func onGameStarted()
    {
        print("game started!")
        activeView = ViewType.GAME
    }
    
    func onGuessingWord() { }
    
    func onRevealNextTile() { }
    
    func onRoundCompleted() { }
    
    func onStartingGame() { }
}
