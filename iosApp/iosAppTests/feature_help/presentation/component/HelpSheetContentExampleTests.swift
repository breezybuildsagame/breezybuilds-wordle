//
//  HelpSheetContentExampleTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 5/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
import SwiftUI
import ViewInspector
@testable import shared
@testable import iosApp

final class HelpSheetContentExampleTests: XCTestCase
{
    let mockTiles = [
        HelpSheetContent.Tile(letter: "T", state: HelpSheet.TileState.correct),
        HelpSheetContent.Tile(letter: "E", state: HelpSheet.TileState.hidden),
        HelpSheetContent.Tile(letter: "S", state: HelpSheet.TileState.hidden),
        HelpSheetContent.Tile(letter: "T", state: HelpSheet.TileState.hidden),
        HelpSheetContent.Tile(letter: "S", state: HelpSheet.TileState.hidden),
    ]
    
    func test_when_Example_initialized_with_tiles__expected_HStack_is_displayed() throws
    {
        // given
        let tilesList = mockTiles
        let sut = HelpSheetContent.Example(tiles: tilesList)
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(
            width: mockFrame().width,
            height: mockFrame().height,
            screenSize: CGSize(width: mockScreen().width, height: mockScreen().height)
        )
        let displayedTileRow = try sut.inspect().view(HelpSheetContent.Example.self).vStack().hStack(0)
        
        // then
        XCTAssertEqual(mockFrame().width * (6.0 / idealFrame().width), try displayedTileRow.spacing())
        
        for (index, tile) in tilesList.enumerated()
        {
            XCTAssertEqual(tile.letter, try displayedTileRow.forEach(0).view(HelpSheetContent.Tile.self, index).actualView().letter)
        }
        
        XCTAssertNoThrow(try displayedTileRow.spacer(1))
    }
    
    func test_when_Example_initialized_with_description__expected_description_is_displayed() throws
    {
        // given
        let expectedDescription = "Testing out **W** description line."
        let sut = HelpSheetContent.Example(tiles: mockTiles, description: expectedDescription)
        let dimensions = SceneDimensions()
        let expectedFont = Font.custom("Roboto-Regular", size: mockFrame().height * (15.0 / idealFrame().height))
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(
            width: mockFrame().width,
            height: mockFrame().height,
            screenSize: CGSize(width: mockScreen().width, height: mockScreen().height)
        )
        let displayedColumn = try sut.inspect().view(HelpSheetContent.Example.self).vStack()
        let displayedText = try displayedColumn.text(1)
        
        // then
        XCTAssertEqual(expectedDescription, try displayedText.string())
        XCTAssertEqual(mockFrame().height * (15.0 / idealFrame().height), try displayedColumn.spacing())
        XCTAssertEqual(.leading, try displayedText.multilineTextAlignment())
        XCTAssertEqual(.ui.onBackground, try displayedText.attributes().foregroundColor())
        XCTAssertEqual(try expectedFont.name(), try displayedText.attributes().font().name())
        XCTAssertEqual(try expectedFont.size(), try displayedText.attributes().font().size())
    }
}
