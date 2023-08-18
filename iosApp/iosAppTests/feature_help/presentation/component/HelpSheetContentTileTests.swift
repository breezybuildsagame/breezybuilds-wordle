//
//  HelpSheetTileTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 5/3/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
import SwiftUI
import ViewInspector
@testable import iosApp
@testable import shared

final class HelpSheetContentTileTests: XCTestCase
{
    func test_when_tile_appears__view_matches_design_requirements()
    {
        // given
        let sut = HelpSheetContent.Tile()
        let expectedSize = mockFrame().height * (50.0 / idealFrame().height)
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertNoThrow(try sut.inspect().find(CoreTile.self).zStack())
        XCTAssertEqual(expectedSize, try sut.inspect().find(CoreTile.self).zStack().fixedWidth())
        XCTAssertEqual(expectedSize, try sut.inspect().find(CoreTile.self).zStack().fixedHeight())
    }
    
    func test_when_hidden_tile_appears__view_matches_design_requirements()
    {
        // given
        let sut = HelpSheetContent.Tile(state: HelpSheet.TileState.hidden)
        let expectedBorderSize = mockFrame().height * (50.0 / idealFrame().height) * (2.0 / 61.0)
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(expectedBorderSize, try sut.inspect().find(CoreTile.self).zStack().border(Color.self).width)
        XCTAssertEqual(Color.clear, try sut.inspect().find(CoreTile.self).zStack().background().color().value())
    }
    
    func test_when_tile_with_letter_appears__view_matches_design_requirements() throws
    {
        // given
        let expectedFontSize = mockFrame().height * (50.0 / idealFrame().height) * (35.0 / 61.0)
        let sut = HelpSheetContent.Tile(letter: "G")
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
        let sut = HelpSheetContent.Tile(state: HelpSheet.TileState.incorrect)
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(0, try sut.inspect().find(CoreTile.self).zStack().border(Color.self).width)
        XCTAssertEqual(.ui.error, try sut.inspect().find(CoreTile.self).zStack().background().color().value())
    }
    
    func test_when_close_tile_appears__view_matches_design_requirements()
    {
        // given
        let sut = HelpSheetContent.Tile(state: HelpSheet.TileState.close)
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(0, try sut.inspect().find(CoreTile.self).zStack().border(Color.self).width)
        XCTAssertEqual(.ui.tertiary, try sut.inspect().find(CoreTile.self).zStack().background().color().value())
    }
    
    func test_when_correct_tile_appears__view_matches_design_requirements()
    {
        // given
        let sut = HelpSheetContent.Tile(state: HelpSheet.TileState.correct)
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(0, try sut.inspect().find(CoreTile.self).zStack().border(Color.self).width)
        XCTAssertEqual(.ui.secondary, try sut.inspect().find(CoreTile.self).zStack().background().color().value())
    }
}
