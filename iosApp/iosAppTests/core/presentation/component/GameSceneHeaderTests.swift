//
//  GameSceneHeaderTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 1/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
import ViewInspector
@testable import iosApp

final class GameSceneHeaderTests: XCTestCase
{
    func test_when_view_appears__the_outermost_element_is_a_vStack()
    {
        // given
        let sut = GameSceneHeader()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(SceneDimensions()))
        
        // then
        XCTAssertNoThrow(try sut.inspect().vStack())
    }
    
    func test_when_view_appears__the_first_element_within_the_vStack_is_a_Spacer()
    {
        // given
        let sut = GameSceneHeader()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(SceneDimensions()))
        
        // then
        XCTAssertNoThrow(try sut.inspect().vStack().spacer(0))
    }
    
    func test_when_view_appears__the_Spacer_size_matches_design_requirements()
    {
        // given
        let expectedWidth: CGFloat = mockFrame().width * (390.0 / idealFrame().width)
        let expectedHeight: CGFloat = mockFrame().height * (46.0 / idealFrame().height)
        let sceneDimensions = SceneDimensions()
        let sut = GameSceneHeader()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(sceneDimensions))
        sceneDimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(expectedWidth, try sut.inspect().vStack().spacer(0).fixedWidth())
        XCTAssertEqual(expectedHeight, try sut.inspect().vStack().spacer(0).fixedHeight())
    }
}
