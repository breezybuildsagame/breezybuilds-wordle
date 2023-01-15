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
}
