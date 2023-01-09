//
//  ContentViewTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 1/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
import SwiftUI
import SwiftUIRouter
import ViewInspector
@testable import iosApp

final class ContentViewTests: XCTestCase
{
    func test_when_view_appears__sizer_utility_color_is_clear_and_the_furthest_back_element() throws
    {
        // given
        let expectedSizerViewColor = Color.ui.primary
        let sut = ContentView()
        let navigator = Navigator(initialPath: "")

        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: ContentView().environmentObject(navigator) )
        
        // then
        XCTAssertEqual(
            expectedSizerViewColor,
            try sut.inspect().zStack().color(0).value()
        )
    }
    
    func test_when_sizer_width_changes__sceneDimensions_updated_to_expected_values() throws
    {
        // given
        let sut = ContentView()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: Router { sut })
        
        // then
        XCTAssertTrue(SceneDimensions.shared.width != 0)
        XCTAssertTrue(SceneDimensions.shared.height != 0)
    }
}
