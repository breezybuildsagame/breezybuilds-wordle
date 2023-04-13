//
//  GameSceneKeyboardTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 1/15/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
import SwiftUI
import ViewInspector
@testable import iosApp
@testable import shared

final class GameSceneKeyboardTests: XCTestCase
{
    var dimensions: SceneDimensions!
    
    override func setUpWithError() throws
    {
        dimensions = SceneDimensions()
        
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
        let expectedFrameWidth = mockScreen().width * (33.0 / idealFrame().width)
        let expectedFrameHeight = mockFrame().height * (56.0 / idealFrame().height)
        let sut = GameSceneKeyboard.Key()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(
            width: mockFrame().width,
            height: mockFrame().height,
            screenSize: CGSize(width: mockScreen().width, height: mockScreen().height)
        )
        
        // then
        XCTAssertEqual(expectedCornerRadius, try sut.inspect().button().labelView().zStack().cornerRadius())
        XCTAssertEqual(expectedFrameWidth, try sut.inspect().button().labelView().zStack().fixedWidth())
        XCTAssertEqual(expectedFrameHeight, try sut.inspect().button().labelView().zStack().fixedHeight())
    }
    
    func test_when_key_appears_with_letter__expected_text_is_displayed() throws
    {
        // given
        let sut = GameSceneKeyboard.Key(letters: "K")
        let expectedFontSize = mockFrame().height * (13.0 / idealFrame().height)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        let textViewAttributes = try sut.inspect().find(text: "K").attributes()
        
        // then
        XCTAssertEqual(.ui.onBackground, try textViewAttributes.foregroundColor())
        XCTAssertEqual("Roboto-Bold", try textViewAttributes.font().name())
        XCTAssertEqual(expectedFontSize, try textViewAttributes.font().size())
    }
    
    func test_when_enter_key_appears__shape_matches_design_requirements()
    {
        // given
        let expectedFrameWidth = mockScreen().width * (52.0 / idealFrame().width)
        let expectedFrameHeight = mockFrame().height * (56.0 / idealFrame().height)
        let sut = GameSceneKeyboard.Key(letters: "ENTER")
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(
            width: mockFrame().width,
            height: mockFrame().height,
            screenSize: CGSize(width: mockScreen().width, height: mockScreen().height)
        )
        
        // then
        XCTAssertEqual(expectedFrameWidth, try sut.inspect().button().labelView().zStack().fixedWidth())
        XCTAssertEqual(expectedFrameHeight, try sut.inspect().button().labelView().zStack().fixedHeight())
    }
    
    func test_when_backspace_key_appears__shape_matches_design_requirements()
    {
        // given
        let expectedFrameWidth = mockScreen().width * (52.0 / idealFrame().width)
        let expectedFrameHeight = mockFrame().height * (56.0 / idealFrame().height)
        let expectedImageSize = mockFrame().width * (23.0 / idealFrame().width)
        let sut = GameSceneKeyboard.Key(resourceId: "game_image_backspace")
        let expectedView = Image("game_image_backspace").resizable()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(
            width: mockFrame().width,
            height: mockFrame().height,
            screenSize: CGSize(width: mockScreen().width, height: mockScreen().height)
        )
        
        // then
        XCTAssertEqual(expectedFrameWidth, try sut.inspect().button().labelView().zStack().fixedWidth())
        XCTAssertEqual(expectedFrameHeight, try sut.inspect().button().labelView().zStack().fixedHeight())
        XCTAssertEqual(expectedView.self, try sut.inspect().button().labelView().zStack().image(1).actualImage())
        XCTAssertTrue(try sut.inspect().button().labelView().zStack().image(1).isScaledToFit())
        XCTAssertEqual(expectedImageSize, try sut.inspect().button().labelView().zStack().image(1).fixedWidth())
        XCTAssertEqual(expectedImageSize, try sut.inspect().button().labelView().zStack().image(1).fixedHeight())
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
        XCTAssertEqual(.ui.background, try sut.inspect().button().labelView().zStack().color(0).value())
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
        XCTAssertEqual(.ui.error, try sut.inspect().button().labelView().zStack().color(0).value())
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
        XCTAssertEqual(.ui.tertiary, try sut.inspect().button().labelView().zStack().color(0).value())
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
        XCTAssertEqual(.ui.secondary, try sut.inspect().button().labelView().zStack().color(0).value())
    }
    
    func test_when_view_appears_and_is_tapped__expected_function_is_invoked() throws
    {
        // given
        var tappedDidInvoke = false
        let sut = GameSceneKeyboard.Key() { tappedDidInvoke = true }
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        try sut.inspect().button().tap()
        
        // then
        XCTAssertTrue(tappedDidInvoke)
    }
    
    func test_when_view_appears_with_rows__view_matches_design_requirements() throws
    {
        // given
        let expectedRowViews = [
            [GameSceneKeyboard.Key(letters: "T"), GameSceneKeyboard.Key(letters: "E")],
            [GameSceneKeyboard.Key(letters: "S"), GameSceneKeyboard.Key(letters: "T")]
        ]
        let expectedKeyHorizontalSpacing = mockFrame().width * (4.0 / idealFrame().width)
        let expectedKeyRowVerticalSpacing = mockFrame().height * (6.0 / idealFrame().height)
        let sut = GameSceneKeyboard(rows: expectedRowViews)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(expectedKeyRowVerticalSpacing, try sut.inspect().zStack().vStack(0).spacing())
        for index in (0..<expectedRowViews.count)
        {
            let keyRow = try sut.inspect().zStack().vStack(0).forEach(0).hStack(index)
            
            XCTAssertEqual(expectedKeyHorizontalSpacing, try keyRow.spacing())
            XCTAssertNoThrow(try sut.inspect().zStack().vStack(0).forEach(0).hStack(index))
            for (viewIndex, view) in expectedRowViews[index].enumerated()
            {
                XCTAssertEqual(
                    view,
                    try keyRow.forEach(0).view(GameSceneKeyboard.Key.self, viewIndex).actualView()
                )
            }
        }
    }
}
