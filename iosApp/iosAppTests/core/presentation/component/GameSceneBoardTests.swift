//
//  GameSceneBoardTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 1/12/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
import ViewInspector
import SwiftUI
@testable import iosApp
@testable import shared

final class GameSceneBoardTests: XCTestCase
{
    func test_when_tile_appears__view_matches_design_requirements()
    {
        // given
        let sut = GameSceneBoard.Tile()
        let expectedSize = mockFrame().height * (61.0 / idealFrame().height)
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertNoThrow(try sut.inspect().zStack())
        XCTAssertEqual(expectedSize, try sut.inspect().zStack().fixedWidth())
        XCTAssertEqual(expectedSize, try sut.inspect().zStack().fixedHeight())
    }
    
    func test_when_hidden_tile_appears__view_matches_design_requirements()
    {
        // given
        let sut = GameSceneBoard.Tile(state: GameBoard.TileState.hidden)
        let expectedBorderSize = mockFrame().height * (2.0 / idealFrame().height)
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(expectedBorderSize, try sut.inspect().zStack().border(Color.self).width)
        XCTAssertEqual(Color.clear, try sut.inspect().zStack().background().color().value())
    }
    
    func test_when_tile_with_letter_appears__view_matches_design_requirements() throws
    {
        // given
        let expectedFontSize = mockFrame().height * (35.0 / idealFrame().height)
        let sut = GameSceneBoard.Tile(letter: "G")
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        let textAttributes = try sut.inspect().find(text: "G").attributes()
        
        // then
        XCTAssertEqual(expectedFontSize, try textAttributes.font().size())
        XCTAssertEqual("Roboto-Bold", try textAttributes.font().name())
        XCTAssertEqual(.ui.onPrimary, try textAttributes.foregroundColor())
    }
    
    func test_when_incorrect_tile_appears__view_matches_design_requirements()
    {
        // given
        let sut = GameSceneBoard.Tile(state: GameBoard.TileState.incorrect)
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(0, try sut.inspect().zStack().border(Color.self).width)
        XCTAssertEqual(.ui.error, try sut.inspect().zStack().background().color().value())
    }
}
