//
//  GameSceneHandlerTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 1/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
@testable import iosApp
@testable import shared

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
}
