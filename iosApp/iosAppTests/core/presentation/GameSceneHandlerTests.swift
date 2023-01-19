//
//  GameSceneHandlerTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 1/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
import ViewInspector
@testable import iosApp
@testable import shared
import SwiftUI

final class GameSceneHandlerTests: XCTestCase
{
    override func setUpWithError() throws
    {
        KoinPlatformManager.shared.stop()
        KoinPlatformManager.shared.start(
            scenarios: [
                Scenario.wordFound,
                Scenario.answerSaved
            ]
        )
    }
    
    override class func tearDown() { KoinPlatformManager.shared.stop() }
    
    func test_when_initialized__activeView_is_Empty()
    {
        // given
        let expectedActiveView = GameSceneHandler.ViewType.EMPTY
        
        // when
        let sut = GameSceneHandler()
        
        // then
        XCTAssertEqual(expectedActiveView, sut.activeView)
        XCTAssertFalse(sut.gameKeyboardIsEnabled)
    }
    
    func test_when_setUp_invoked__viewModel_setUp_is_invoked()
    {
        // given
        let expectedActiveView = GameSceneHandler.ViewType.GAME
        let sut = GameSceneHandler()
        
        // when
        sut.setUp()
        
        // then
        XCTAssertEqual(expectedActiveView, sut.activeView)
        XCTAssertTrue(sut.gameKeyboardIsEnabled)
    }
    
    func test_when_gameHeader_view_appears__title_matches_expected_title()
    {
        // given
        let expectedTitle = GameSceneViewModel().getHeader().title()
        let sut = GameSceneHandler()
        let actualHeader = sut.gameHeader()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: actualHeader.environmentObject(SceneDimensions()))
        
        // then
        XCTAssertEqual(expectedTitle,try actualHeader.inspect().find(text: expectedTitle).string())
    }
    
    func test_when_gameBoard_view_appears__rows_match_expected_value()
    {
        // given
        let expectedRowCount = GameSceneViewModel().getGameBoard().rows().count
        let sut = GameSceneHandler()
        let actualBoard = sut.gameBoard()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: actualBoard.environmentObject(SceneDimensions()))
        
        // then
        XCTAssertEqual(expectedRowCount, try actualBoard.inspect().vStack().forEach(0).count)
    }
    
    func test_when_gameKeyboard_view_appears__rows_match_expected_value()
    {
        // given
        let expectedRowCount = GameSceneViewModel().getGameKeyboard().rows().count
        let sut = GameSceneHandler()
        let actualKeyboard = sut.gameKeyboard()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: actualKeyboard.environmentObject(SceneDimensions()))
        
        // then
        XCTAssertEqual(expectedRowCount, try actualKeyboard.inspect().zStack().vStack(0).forEach(0).count)
    }
    
    func test_when_gameAnnouncement_view_appears__text_matches_expected_value()
    {
        // given
        let expectedTextValue = "My Middle Layer Announcement"
        let actualAnnouncement = GameSceneViewModel().getAnnouncement()
        let sut = GameSceneHandler()
        actualAnnouncement.setMessage(newMessage: expectedTextValue)
        let actualAnnouncementView = sut.gameAnnouncement()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: actualAnnouncementView.environmentObject(SceneDimensions()))
        
        // then
        XCTAssertEqual(expectedTextValue, try actualAnnouncementView.inspect().text().string())
    }
    
    func test_when_gameAnnouncement_contains_no_announcement__view_is_not_returned()
    {
        // given
        let actualAnnouncement = GameSceneViewModel().getAnnouncement()
        actualAnnouncement.setMessage(newMessage: nil)
        
        // when
        let actualAnnouncementView = GameSceneHandler().gameAnnouncement()
        
        // then
        XCTAssertNil(actualAnnouncementView)
    }
    
    func test_when_onGameOver_invoked__active_view_is_published_to_GAME()
    {
        // given
        let actualAnnouncement = GameSceneViewModel().getAnnouncement()
        let sut = GameSceneHandler()
        sut.onGameStarted()
        let expectation = XCTestExpectation(description: "Waiting for onGameOver to publish.")
        let mockView = MockView(handler: sut, expectation: expectation)
        defer { ViewHosting.expel() }
        ViewHosting.host(view: mockView)
        
        // when
        actualAnnouncement.setMessage(newMessage: "Game over, man!")
        sut.onGameOver()
        
        // then
        wait(for: [expectation], timeout: 1.0)
        XCTAssertEqual(.GAME, sut.activeView)
        XCTAssertFalse(sut.gameKeyboardIsEnabled)
        XCTAssertNoThrow(try mockView.inspect().find(text: "Game over, man!"))
    }
    
    func test_when_onRevealNextTile_invoked__active_view_is_published_to_GAME()
    {
        // given
        let actualAnnouncement = GameSceneViewModel().getAnnouncement()
        let sut = GameSceneHandler()
        sut.onGameStarted()
        let expectation = XCTestExpectation(description: "Waiting for onRevealNextTile to publish.")
        let mockView = MockView(handler: sut, expectation: expectation)
        defer { ViewHosting.expel() }
        ViewHosting.host(view: mockView)
        
        // when
        actualAnnouncement.setMessage(newMessage: "Revealing the next tile!")
        sut.onRevealNextTile()
        
        // then
        wait(for: [expectation], timeout: 1.0)
        XCTAssertEqual(.GAME, sut.activeView)
        XCTAssertTrue(sut.gameKeyboardIsEnabled)
        XCTAssertNoThrow(try mockView.inspect().find(text: "Revealing the next tile!"))
    }
    
    func test_when_onRoundCompleted_invoked__active_view_is_published_to_GAME()
    {
        // given
        let actualAnnouncement = GameSceneViewModel().getAnnouncement()
        let sut = GameSceneHandler()
        sut.onGameStarted()
        let expectation = XCTestExpectation(description: "Waiting for onRoundCompleted to publish.")
        let mockView = MockView(handler: sut, expectation: expectation)
        defer { ViewHosting.expel() }
        ViewHosting.host(view: mockView)
        
        // when
        actualAnnouncement.setMessage(newMessage: "Next Round!")
        sut.onRoundCompleted()
        
        // then
        wait(for: [expectation], timeout: 1.0)
        XCTAssertEqual(.GAME, sut.activeView)
        XCTAssertTrue(sut.gameKeyboardIsEnabled)
        XCTAssertNoThrow(try mockView.inspect().find(text: "Next Round!"))
    }
    
    func test_when_onStartingGame_invoked__active_view_is_published_to_GAME()
    {
        // given
        let actualAnnouncement = GameSceneViewModel().getAnnouncement()
        let sut = GameSceneHandler()
        sut.onGameStarted()
        let expectation = XCTestExpectation(description: "Waiting for onStartingGame to publish.")
        let mockView = MockView(handler: sut, expectation: expectation)
        defer { ViewHosting.expel() }
        ViewHosting.host(view: mockView)
        
        // when
        actualAnnouncement.setMessage(newMessage: "Start Game!")
        sut.onStartingGame()
        
        // then
        wait(for: [expectation], timeout: 1.0)
        XCTAssertEqual(.GAME, sut.activeView)
        XCTAssertFalse(sut.gameKeyboardIsEnabled)
        XCTAssertNoThrow(try mockView.inspect().find(text: "Start Game!"))
    }
    
    func test_when_onGuessingWord_invoked__active_view_is_published_to_GAME()
    {
        // given
        let actualAnnouncement = GameSceneViewModel().getAnnouncement()
        let sut = GameSceneHandler()
        sut.onGameStarted()
        let expectation = XCTestExpectation(description: "Waiting for onGuessingWord to publish.")
        let mockView = MockView(handler: sut, expectation: expectation)
        defer { ViewHosting.expel() }
        ViewHosting.host(view: mockView)
        
        // when
        actualAnnouncement.setMessage(newMessage: "Guessing word, hold your ponies!")
        sut.onGuessingWord()
        
        // then
        wait(for: [expectation], timeout: 1.0)
        XCTAssertEqual(.GAME, sut.activeView)
        XCTAssertFalse(sut.gameKeyboardIsEnabled)
        XCTAssertNoThrow(try mockView.inspect().find(text: "Guessing word, hold your ponies!"))
    }
    
    func test_when_gameKeyboard_is_enabled_and_key_clicked__expected_function_is_invoked() throws
    {
        // given
        let sut = GameSceneHandler()
        let keyboardView = sut.gameKeyboard()
        sut.setUp()
        
        defer { ViewHosting.expel() }
        ViewHosting.host(view: keyboardView.environmentObject(SceneDimensions()))
        
        // when
        sut.activeView = .EMPTY
        let key = try keyboardView.inspect().find(GameSceneKeyboard.Key.self, where: { try $0.actualView().letters == "K" } )
        try key.button().tap()
        
        // then
        XCTAssertEqual(.GAME, sut.activeView)
    }
    
    func test_when_gameKeyboard_is_not_enabled_and_key_clicked__no_function_is_invoked() throws
    {
        // given
        let sut = GameSceneHandler()
        let keyboardView = sut.gameKeyboard()
        sut.setUp()
        sut.onGuessingWord()
        
        defer { ViewHosting.expel() }
        ViewHosting.host(view: keyboardView.environmentObject(SceneDimensions()))
        
        // when
        sut.activeView = .EMPTY
        let key = try keyboardView.inspect().find(GameSceneKeyboard.Key.self, where: { try $0.actualView().letters == "K" } )
        try key.button().tap()
        
        // then
        XCTAssertEqual(.EMPTY, sut.activeView)
    }
    
    struct MockView: View
    {
        @ObservedObject var handler: GameSceneHandler
        
        var expectation: XCTestExpectation
        
        var body: some View
        {
            Text(handler.gameAnnouncement()?.text ?? "")
                .onReceive(handler.$activeView)
            { newActiveView in
                if newActiveView != .EMPTY { expectation.fulfill() }
            }
        }
    }
}
