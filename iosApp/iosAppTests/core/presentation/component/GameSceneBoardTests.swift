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
    }
}
