//
//  GameSceneViewModelMock.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 8/14/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class GameSceneViewModelMock: GameSceneViewModelRepresentable
{
    var announcementToReturn = AnnouncementMock("Mock Announcement")
    var gameBoardToReturn = GameBoard(rows: [])
    var gameKeyboardToReturn = GameKeyboard()
    var gameHeaderToReturn = GameHeader(title: "Mock Header", options: [])
    var setUpPassedInHandler:  GameSceneHandleable? = nil
    
    func getAnnouncement() -> AnnouncementRepresentable { announcementToReturn }
    
    func getGameBoard() async throws -> GameBoard { gameBoardToReturn }
    
    func getGameKeyboard() -> GameKeyboard { gameKeyboardToReturn }
    
    func getHeader() -> GameHeader { gameHeaderToReturn }
    
    func setUp(gameSceneHandler: GameSceneHandleable?) async throws {
        setUpPassedInHandler = gameSceneHandler
    }
    
    class AnnouncementMock: AnnouncementRepresentable
    {
        var messageToReturn: String?
        var setMessagePassedInNewMessage: String? = nil
        
        init(_ message: String) { self.messageToReturn = message }
        
        func message() -> String? { messageToReturn }
        
        func setMessage(newMessage: String?) {
            setMessagePassedInNewMessage = newMessage }
    }
}
