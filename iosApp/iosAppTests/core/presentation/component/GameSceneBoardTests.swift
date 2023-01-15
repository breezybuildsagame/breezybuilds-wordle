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
    
    func test_when_close_tile_appears__view_matches_design_requirements()
    {
        // given
        let sut = GameSceneBoard.Tile(state: GameBoard.TileState.close)
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(0, try sut.inspect().zStack().border(Color.self).width)
        XCTAssertEqual(.ui.tertiary, try sut.inspect().zStack().background().color().value())
    }
    
    func test_when_correct_tile_appears__view_matches_design_requirements()
    {
        // given
        let sut = GameSceneBoard.Tile(state: GameBoard.TileState.correct)
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(0, try sut.inspect().zStack().border(Color.self).width)
        XCTAssertEqual(.ui.secondary, try sut.inspect().zStack().background().color().value())
    }
    
    func test_when_view_appears_with_rows__view_matches_design_requirements() throws
    {
        // given
        let expectedHStackSpacing = mockFrame().width * (7.0 / idealFrame().width)
        let expectedVStackSpacing = mockFrame().height * (10.0 / idealFrame().height)
        let rows: [[GameBoard.Tile]] = [
            [GameBoard.Tile(letter: "G", state: .incorrect), GameBoard.Tile(letter: "O", state: .close),
                GameBoard.Tile(letter: "O", state: .hidden), GameBoard.Tile(letter: "D", state: .correct)],
            [GameBoard.Tile(letter: "H", state: .hidden), GameBoard.Tile(letter: "O", state: .hidden),
                GameBoard.Tile(letter: "O", state: .hidden), GameBoard.Tile(letter: "D", state: .hidden)]
        ]
        let sut = GameSceneBoard(rows: rows)
        let sceneDimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(sceneDimensions))
        sceneDimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        let firstRow = try sut.inspect().vStack().forEach(0).hStack(0).forEach(0)
        let secondRow = try sut.inspect().vStack().forEach(0).hStack(1).forEach(0)
        
        
        // then
        XCTAssertNoThrow(try sut.inspect().vStack(0))
        XCTAssertNoThrow(try sut.inspect().vStack().forEach(0).hStack(0))
        XCTAssertNoThrow(try sut.inspect().vStack().forEach(0).hStack(1))
        XCTAssertEqual(expectedVStackSpacing, try sut.inspect().vStack(0).spacing())
        XCTAssertEqual(expectedHStackSpacing, try sut.inspect().vStack().forEach(0).hStack(0).spacing())
        XCTAssertEqual(expectedHStackSpacing, try sut.inspect().vStack().forEach(0).hStack(1).spacing())
        for i in 0..<firstRow.count
        {
            XCTAssertNoThrow(try firstRow.view(GameSceneBoard.Tile.self, i))
            XCTAssertEqual(String(describing: rows[0][i].letter()), try firstRow.view(GameSceneBoard.Tile.self, i).actualView().letter)
        }
        for i in 0..<secondRow.count
        {
            XCTAssertNoThrow(try firstRow.view(GameSceneBoard.Tile.self, i))
            XCTAssertEqual(String(describing: rows[0][i].letter()), try firstRow.view(GameSceneBoard.Tile.self, i).actualView().letter)
        }
    }
}
