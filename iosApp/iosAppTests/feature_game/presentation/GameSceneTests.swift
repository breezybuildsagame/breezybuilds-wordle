//
//  GameSceneTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 1/7/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
import ViewInspector
@testable import iosApp
@testable import shared

final class GameSceneTests: XCTestCase
{
    override func setUpWithError() throws {
        KoinPlatformManager.shared.start(scenarios: [Scenario.wordFound, Scenario.answerSaved])
    }
    
    func test_when_view_appears__handler_setUp_is_invoked()
    {
        // given
        let sut = GameScene()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(SceneDimensions()))
        
        // then
        XCTAssertNoThrow(try sut.inspect().zStack().zStack(0).vStack(0).view(GameSceneHeader.self, 0))
        XCTAssertNoThrow(try sut.inspect().zStack().zStack(0).vStack(0).spacer(1))
        XCTAssertNoThrow(try sut.inspect().zStack().zStack(0).vStack(0).view(GameSceneBoard.self, 2))
        XCTAssertNoThrow(try sut.inspect().zStack().zStack(0).vStack(0).spacer(3))
        XCTAssertNoThrow(try sut.inspect().zStack().zStack(0).vStack(0).view(GameSceneKeyboard.self, 4))
    }
    
    func test_when_announcement_appears__expected_announcement_is_displayed()
    {
        // given
        let expectedTopSpacerHeight = mockFrame().height * (200 / idealFrame().height)
        let sut = GameScene()
        let sceneDimensions = SceneDimensions()
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(sceneDimensions))
        sceneDimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // when
        GameSceneViewModel().getAnnouncement().setMessage(newMessage: "Game Over, man!")
        GameSceneHandler.shared.onGameOver()
        
        // then
        XCTAssertNoThrow(try sut.inspect().zStack().zStack(0).vStack(1).spacer(0))
        XCTAssertEqual(expectedTopSpacerHeight, try sut.inspect().zStack().zStack(0).vStack(1).spacer(0).fixedHeight())
        XCTAssertNoThrow(try sut.inspect().zStack().zStack(0).vStack(1).view(GameSceneAnnouncement.self, 1))
        XCTAssertNoThrow(try sut.inspect().zStack().zStack(0).vStack(1).spacer(2))
    }
}
