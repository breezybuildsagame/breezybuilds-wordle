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
    func test_when_Example_initialized_with_tiles__expected_HStack_is_displayed() throws
    {
        // given
        let tilesList = [
            HelpSheetContent.Tile(letter: "T", state: HelpSheet.TileState.correct),
            HelpSheetContent.Tile(letter: "E", state: HelpSheet.TileState.hidden),
            HelpSheetContent.Tile(letter: "S", state: HelpSheet.TileState.hidden),
            HelpSheetContent.Tile(letter: "T", state: HelpSheet.TileState.hidden),
            HelpSheetContent.Tile(letter: "S", state: HelpSheet.TileState.hidden),
        ]
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
        let displayedTileRow = try sut.inspect().view(HelpSheetContent.Example.self).hStack()
        
        // then
        XCTAssertEqual(mockFrame().width * (6.0 / idealFrame().width), try displayedTileRow.spacing())
        XCTAssertEqual(mockFrame().height * (15.0 / idealFrame().height), try displayedTileRow.padding(.bottom))
        
        for (index, tile) in tilesList.enumerated()
        {
            XCTAssertEqual(tile.letter, try displayedTileRow.forEach(0).view(HelpSheetContent.Tile.self, index).actualView().letter)
        }
        
        XCTAssertNoThrow(try displayedTileRow.spacer(1))
    }
    
    func test_when_Example_initialized_with_description__expected_description_is_displayed()
    {
        // given
        
        // when
        
        // then
    }
}
