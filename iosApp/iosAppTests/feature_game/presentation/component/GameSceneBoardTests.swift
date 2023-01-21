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
    
    func test_when_view_appears_with_Rows__view_matches_design_requirements() throws
    {
        // given
        let expectedVStackSpacing = mockFrame().height * (10.0 / idealFrame().height)
        let rows: [GameSceneBoard.Row] = [GameSceneBoard.Row(tiles: [
                GameSceneBoard.Tile(letter: "G", state: .correct),
                GameSceneBoard.Tile(letter: "O", state: .close),
                GameSceneBoard.Tile(letter: "O", state: .incorrect),
                GameSceneBoard.Tile(letter: "D", state: .incorrect),
                GameSceneBoard.Tile(letter: "Y", state: .incorrect)
            ]),
            GameSceneBoard.Row(tiles: [
                GameSceneBoard.Tile(letter: "H", state: .correct),
                GameSceneBoard.Tile(letter: "O", state: .close),
                GameSceneBoard.Tile(letter: "O", state: .incorrect),
                GameSceneBoard.Tile(letter: "D", state: .incorrect),
                GameSceneBoard.Tile(letter: "", state: .hidden)
            ])
        ]
        let sut = GameSceneBoard(rows: rows)
        let sceneDimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(sceneDimensions))
        sceneDimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        let firstRow = try sut.inspect().vStack().forEach(0).view(GameSceneBoard.Row.self, 0)
        let secondRow = try sut.inspect().vStack().forEach(0).view(GameSceneBoard.Row.self, 1)
        
        
        // then
        XCTAssertEqual(expectedVStackSpacing, try sut.inspect().vStack(0).spacing())
        for i in 0..<firstRow.count
        {
            XCTAssertEqual(
                rows[0].tiles[i].letter,
                try firstRow.hStack().forEach(0).view(GameSceneBoard.Tile.self, i).actualView().letter
            )
        }
        for i in 0..<secondRow.count
        {
            XCTAssertEqual(
                rows[1].tiles[i].letter,
                try secondRow.hStack().forEach(0).view(GameSceneBoard.Tile.self, i).actualView().letter
            )
        }
    }
    
    func test_when_Row_appears_with_tiles__expected_views_are_displayed()
    {
        // given
        let expectedTileHorizontalSpacing = mockFrame().width * (7.0 / idealFrame().width)
        let expectedTiles = [
            GameSceneBoard.Tile(letter: "T", state: .correct),
            GameSceneBoard.Tile(letter: "E", state: .close),
            GameSceneBoard.Tile(letter: "S", state: .incorrect),
            GameSceneBoard.Tile(letter: "T", state: .hidden),
            GameSceneBoard.Tile(letter: nil, state: .hidden)
        ]
        let sut = GameSceneBoard.Row(tiles: expectedTiles)
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(expectedTileHorizontalSpacing, try sut.inspect().hStack().spacing())
        for (index, _) in expectedTiles.enumerated()
        {
            XCTAssertEqual(expectedTiles[index].letter, try sut.inspect().hStack().forEach(0).view(GameSceneBoard.Tile.self, index).actualView().letter)
        }
    }
}
