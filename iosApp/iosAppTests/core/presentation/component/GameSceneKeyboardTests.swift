//
//  GameSceneKeyboardTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 1/15/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
import ViewInspector
@testable import iosApp
@testable import shared

final class GameSceneKeyboardTests: XCTestCase
{
    var dimensions: SceneDimensions!
    
    override func setUpWithError() throws
    {
        dimensions = SceneDimensions()
        
        KoinPlatformManager.shared.stop()
        KoinPlatformManager.shared.start(
            scenarios: [
                Scenario.wordFound,
                Scenario.answerSaved
            ]
        )
    }
    
    func test_when_Key_view_appears__shape_matches_design_requirements()
    {
        // given
        let expectedCornerRadius = mockFrame().width * (4.0 / idealFrame().width)
        let expectedFrameWidth = mockFrame().width * (33.0 / idealFrame().width)
        let expectedFrameHeight = mockFrame().height * (56.0 / idealFrame().height)
        let sut = GameSceneKeyboard.Key()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(expectedCornerRadius, try sut.inspect().zStack().cornerRadius())
        XCTAssertEqual(expectedFrameWidth, try sut.inspect().zStack().fixedWidth())
        XCTAssertEqual(expectedFrameHeight, try sut.inspect().zStack().fixedHeight())
    }
    
    func test_when_default_Key_view_appears__color_matches_design_requirements()
    {
        // given
        let sut = GameSceneKeyboard.Key(backgroundColor: GameKeyboard.KeyBackgroundColor.default_)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(.ui.background, try sut.inspect().zStack().color(0).value())
    }
    
    func test_when_not_found_Key_view_appears__color_matches_design_requirements()
    {
        // given
        let sut = GameSceneKeyboard.Key(backgroundColor: GameKeyboard.KeyBackgroundColor.notFound)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(.ui.error, try sut.inspect().zStack().color(0).value())
    }
        
    func test_when_nearby_Key_view_appears__color_matches_design_requirements()
    {
        // given
        let sut = GameSceneKeyboard.Key(backgroundColor: GameKeyboard.KeyBackgroundColor.nearby)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(.ui.tertiary, try sut.inspect().zStack().color(0).value())
    }
    
    func test_when_correct_Key_view_appears__color_matches_design_requirements()
    {
        // given
        let sut = GameSceneKeyboard.Key(backgroundColor: GameKeyboard.KeyBackgroundColor.correct)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(.ui.secondary, try sut.inspect().zStack().color(0).value())
    }
}
