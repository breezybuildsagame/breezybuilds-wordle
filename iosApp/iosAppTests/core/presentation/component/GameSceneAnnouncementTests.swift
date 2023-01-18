//
//  GameSceneAnnouncementTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 1/18/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
import SwiftUI
import ViewInspector
@testable import iosApp

final class GameSceneAnnouncementTests: XCTestCase
{
    func test_when_view_appears_with_text__expected_text_is_displayed()
    {
        // given
        let expectedText = "Great new announcement!"
        let sut = GameSceneAnnouncement(text: expectedText)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(SceneDimensions()))
        
        // then
        XCTAssertEqual(expectedText, try sut.inspect().text().string())
    }
    
    func test_when_view_appears__view_matches_design_requirements()
    {
        // given
        let expectedPadding = mockFrame().height * (26.0 / idealFrame().height)
        let expectedFontSize = mockFrame().height * (20.0 / idealFrame().height)
        let expectedShadowRadius = mockFrame().height * (4.0 / idealFrame().height)
        let expectedCornerRadius = mockFrame().height * (4.0 / idealFrame().height)
        let sut = GameSceneAnnouncement(text: "Test")
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(expectedPadding, try sut.inspect().text().padding(.all))
        XCTAssertEqual(Color.ui.primary, try sut.inspect().text().attributes().foregroundColor())
        XCTAssertEqual(Color.ui.onPrimary, try sut.inspect().text().background().color().value())
        XCTAssertEqual("Roboto-Bold", try sut.inspect().text().attributes().font().name())
        XCTAssertEqual(expectedFontSize, try sut.inspect().text().attributes().font().size())
        XCTAssertEqual(Color(red: 0, green: 0, blue: 0, opacity: 0.25), try sut.inspect().text().shadow().color)
        XCTAssertEqual(expectedShadowRadius, try sut.inspect().text().shadow().radius)
        XCTAssertEqual(expectedShadowRadius, try sut.inspect().text().shadow().offset.height)
        XCTAssertEqual(expectedCornerRadius, try sut.inspect().text().cornerRadius())
    }
}
