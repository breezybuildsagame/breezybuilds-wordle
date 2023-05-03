//
//  HelpSheetTileTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 5/3/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
import ViewInspector
@testable import iosApp

final class HelpSheetTileTests: XCTestCase
{
    func test_when_tile_appears__view_matches_design_requirements()
    {
        // given
        let sut = HelpSheet.Tile()
        let expectedSize = mockFrame().height * (50.0 / idealFrame().height)
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
}
