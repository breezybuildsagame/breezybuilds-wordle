//
//  GameSceneHeaderTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 1/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
import SwiftUI
import ViewInspector
@testable import iosApp

final class GameSceneHeaderTests: XCTestCase
{
    func test_when_option_appears__the_outermost_element_is_a_button()
    {
        // given
        let sut = GameSceneHeader.Option()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(SceneDimensions()))
        
        // then
        XCTAssertNoThrow(try sut.inspect().button())
    }
    
    func test_when_option_appears_with_resourceId__the_button_label_contains_an_image()
    {
        // given
        let expectedImageName = "test_image"
        let sut = GameSceneHeader.Option(resourceId: expectedImageName)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(SceneDimensions()))
        
        // then
        XCTAssertNoThrow(try sut.inspect().button().labelView().image())
        XCTAssertEqual(expectedImageName, try sut.inspect().button().labelView().image().actualImage().name())
    }
    
    func test_when_option_appears_with_resourceId__the_image_matches_design_requirements()
    {
        // given
        let expectedSize = mockFrame().width * (24.0 / idealFrame().width)
        let expectedImage = Image("test_image").resizable()
        let sut = GameSceneHeader.Option(resourceId: "test_image")
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(expectedImage, try sut.inspect().button().labelView().image().actualImage())
        XCTAssertTrue(try sut.inspect().button().labelView().image().isScaledToFit())
        XCTAssertEqual(expectedSize, try sut.inspect().button().labelView().image().fixedWidth())
        XCTAssertEqual(expectedSize, try sut.inspect().button().labelView().image().fixedHeight())
    }
    
    func test_when_option_appears_and_is_tapped__expected_function_is_invoked() throws
    {
        // given
        var tapDidInvoke = false
        let sut = GameSceneHeader.Option(onTap: { tapDidInvoke = true })
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(SceneDimensions()))
        try sut.inspect().button().tap()
        
        // then
        XCTAssertTrue(tapDidInvoke)
    }
    
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
    
    func test_when_view_appears__the_second_element_within_the_VStack_is_an_HStack()
    {
        // given
        let sut = GameSceneHeader()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(SceneDimensions()))
        
        // then
        XCTAssertNoThrow(try sut.inspect().vStack().hStack(1))
    }
    
    func test_when_view_appears_with_options__the_first_element_within_the_HStack_is_the_first_option() throws
    {
        // given
        var optionOneWasTapped = false
        var optionTwoWasTapped = false
        let expectedViewType = GameSceneHeader.Option.self
        let sut = GameSceneHeader(
            options: [
                GameSceneHeader.Option() { optionOneWasTapped = true },
                GameSceneHeader.Option() { optionTwoWasTapped = true }
            ]
        )
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(SceneDimensions()))
        try sut.inspect().vStack().hStack(1).view(expectedViewType, 0).button().tap()
        
        // then
        XCTAssertTrue(optionOneWasTapped)
        XCTAssertFalse(optionTwoWasTapped)
    }
    
    func test_when_view_appears_with_title__the_second_element_within_the_HStack_is_Text()
    {
        // given
        let expectedTitle = "WORDLE"
        let sut = GameSceneHeader(title: expectedTitle)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(SceneDimensions()))
        
        // then
        XCTAssertEqual(expectedTitle, try sut.inspect().vStack().hStack(1).text(1).string())
    }
    
    func test_when_view_appears_with_title__title_text_matches_design_requirements() throws
    {
        // given
        let expectedFontSize = mockFrame().height * (40.0 / idealFrame().height)
        let expectedFontColor = Color.ui.onPrimary
        let sut = GameSceneHeader(title: "Test")
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        let textAttributes =  try sut.inspect().vStack().hStack(1).text(1).attributes()
        
        // then
        XCTAssertEqual(expectedFontSize, try textAttributes.font().size())
        XCTAssertEqual(expectedFontColor, try textAttributes.foregroundColor())
        XCTAssertEqual("Roboto-Black", try textAttributes.font().name())
    }
}
