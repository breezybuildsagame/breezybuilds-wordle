//
//  HelpSheetContentTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 8/3/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
import SwiftUI
import ViewInspector
import shared
@testable import iosApp

final class HelpSheetContentTests: XCTestCase
{
    func test_when_view_initialized__expected_content_container_is_displayed() throws
    {
        // given
        let sut = HelpSheetContent()
        let dimensions = SceneDimensions()
        let expectedVstackWidth = mockFrame().width * (350.0 / idealFrame().width)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(expectedVstackWidth, try sut.inspect().view(HelpSheetContent.self).zStack().vStack(0).fixedWidth())
        XCTAssertEqual(.leading, try sut.inspect().view(HelpSheetContent.self).zStack().vStack(0).fixedAlignment())
    }
    
    func test_when_view_initialized_with_title_String_paramter__expected_title_is_displayed() throws
    {
        // given
        let expectedTitle = "TEST HOW TO PLAY"
        let sut = HelpSheetContent(testTitle: expectedTitle)
        let expectedBottomPadding = mockFrame().height * (20.0 / idealFrame().height)
        let expectedHorizontalPadding = mockFrame().width * (25.0 / idealFrame().width)
        let expectedFontSize = mockFrame().height * (20.0 / idealFrame().height)
        let expectedFrameWidth = mockFrame().width * (300.0 / idealFrame().width)
        let expectedFrameHeight = mockFrame().height * (50.0 / idealFrame().height)
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        let textView = try sut.inspect().view(HelpSheetContent.self).zStack().vStack(0).text(0)
        
        // then
        XCTAssertEqual(expectedTitle, try textView.string())
        XCTAssertEqual("Roboto-Black", try textView.attributes().font().name())
        XCTAssertEqual(expectedFontSize, try textView.attributes().font().size())
        XCTAssertEqual(.center, try textView.multilineTextAlignment())
        XCTAssertEqual(.ui.onSurface, try textView.attributes().foregroundColor())
        XCTAssertEqual(expectedFrameWidth, try textView.fixedWidth())
        XCTAssertEqual(expectedFrameHeight, try textView.fixedHeight())
        XCTAssertEqual(expectedBottomPadding, try textView.padding(.bottom))
        XCTAssertEqual(expectedHorizontalPadding, try textView.padding(.horizontal))
    }
    
    func test_when_view_initialized_with_Instruction_list__expected_instructions_are_displayed() throws
    {
        // given
        let expectedDividerHeight = mockFrame().height * (1.0 / idealFrame().height)
        let expectedDividerBottomPadding = mockFrame().height * (20.0 / idealFrame().height)
        let expectedInstructions = [
            HelpSheetContent.Instruction(instruction: "Guess the WORDLE in 6 tries."),
            HelpSheetContent.Instruction(instruction: "Each guess must be a valid 5 letter word. Hit the enter button to submit.")
        ]
        let sut = HelpSheetContent(testInstructions: expectedInstructions)
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
    
        // then
        try expectedInstructions.enumerated().forEach()
        { (index, instruction) in
            let instructionView = try sut.inspect().view(HelpSheetContent.self).zStack().vStack(0).forEach(1).view(HelpSheetContent.Instruction.self, index)
            
            XCTAssertEqual(expectedInstructions[index], try instructionView.actualView())
        }
        let borderView = try sut.inspect().view(HelpSheetContent.self).zStack().vStack(0).color(2)
        XCTAssertEqual(.ui.error, try borderView.value())
        XCTAssertEqual(expectedDividerHeight, try borderView.fixedHeight())
        XCTAssertEqual(expectedDividerBottomPadding, try borderView.padding(.bottom))
    }
    
    func test_when_initialized_with_Example_list__expected_examples_are_displayed() throws
    {
        // given
        let expectedExamples = [
            HelpSheetContent.Example(
                tiles: [
                    HelpSheetContent.Tile(letter: "W", state: HelpSheet.TileState.correct),
                    HelpSheetContent.Tile(letter: "E", state: HelpSheet.TileState.incorrect),
                    HelpSheetContent.Tile(letter: "A", state: HelpSheet.TileState.incorrect),
                    HelpSheetContent.Tile(letter: "R", state: HelpSheet.TileState.incorrect),
                    HelpSheetContent.Tile(letter: "Y", state: HelpSheet.TileState.incorrect),
                ],
                description: "The letter W is in the word and in the correct spot."
            ),
            HelpSheetContent.Example(
                tiles: [
                    HelpSheetContent.Tile(letter: "P", state: HelpSheet.TileState.correct),
                    HelpSheetContent.Tile(letter: "I", state: HelpSheet.TileState.incorrect),
                    HelpSheetContent.Tile(letter: "L", state: HelpSheet.TileState.incorrect),
                    HelpSheetContent.Tile(letter: "L", state: HelpSheet.TileState.incorrect),
                    HelpSheetContent.Tile(letter: "S", state: HelpSheet.TileState.incorrect),
                ],
                description: "The letter I is in the word but in the wrong spot."
            ),
        ]
        let sut = HelpSheetContent(testExamples: expectedExamples)
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        let examplesTitleView = try sut.inspect().view(HelpSheetContent.self).zStack().vStack(0).text(3)
        
        // then
        XCTAssertEqual("Examples", try examplesTitleView.string())
        XCTAssertEqual("Roboto-Regular", try examplesTitleView.attributes().font().name())
        XCTAssertEqual(.ui.onSurface, try examplesTitleView.attributes().foregroundColor())
        XCTAssertEqual(mockFrame().height * (16.0 / idealFrame().height), try examplesTitleView.attributes().font().size())
        XCTAssertEqual(.leading, try examplesTitleView.multilineTextAlignment())
        XCTAssertEqual(mockFrame().height * (20.0 / idealFrame().height), try examplesTitleView.padding(.bottom))
        try expectedExamples.enumerated().forEach()
        { (index, example) in
            let exampleView = try sut.inspect().view(HelpSheetContent.self).zStack().vStack(0).forEach(4).view(HelpSheetContent.Example.self, index)
            
            XCTAssertEqual(expectedExamples[index], try exampleView.actualView())
        }
    }
    
    func test_when_initialized_with_footer_String_parameter__expected_text_is_displayed() throws
    {
        // given
        let expectedFooterText = "No daily restrictions - play as often as you like!"
        let sut = HelpSheetContent(testFooter: expectedFooterText)
        let dimensions = SceneDimensions()
        let expectedFontSize = mockFrame().height * (15.0 / idealFrame().height)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        let textView = try sut.inspect().zStack().vStack(0).find(text: expectedFooterText)
        
        // then
        XCTAssertEqual("Roboto-Black", try textView.attributes().font().name())
        XCTAssertEqual(.ui.onSurface, try textView.attributes().foregroundColor())
        XCTAssertEqual(expectedFontSize, try textView.attributes().font().size())
        XCTAssertEqual(.leading, try textView.multilineTextAlignment())
    }
    
    func test_when_initialized_with_CloseButton__expected_button_is_displayed() throws
    {
        // given
        var buttonClickDidInvoke = false
        let expectedButton = HelpSheetContent.CloseButton(
            imageResourceId: "core_image_close_icon",
            onClick: { buttonClickDidInvoke = true }
        )
        let dimensions = SceneDimensions()
        let sut = HelpSheetContent(testCloseButton: expectedButton)
        let expectedButtonImageSize = mockFrame().height * (25.0 / idealFrame().height)
        let expectedTopPadding = mockFrame().height * (10.0 / idealFrame().height)
        let expectedTrailingPadding = mockFrame().width * (15.0 / idealFrame().width)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        let displayedButton = try sut.inspect().find(HelpSheetContent.CloseButton.self)
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertNoThrow(try displayedButton.hStack().spacer(0))
        try displayedButton.hStack().button(1).tap()
        XCTAssertTrue(buttonClickDidInvoke)
        XCTAssertEqual(expectedButtonImageSize, try displayedButton.hStack().button(1).labelView().image().fixedWidth())
        XCTAssertEqual(expectedButtonImageSize, try displayedButton.hStack().button(1).labelView().image().fixedHeight())
        XCTAssertEqual(expectedTopPadding, try displayedButton.hStack().padding(.top))
        XCTAssertEqual(expectedTrailingPadding, try displayedButton.hStack().padding(.trailing))
        XCTAssertEqual(expectedButton.imageResourceId, try displayedButton.actualView().imageResourceId)
    }
}

extension HelpSheetContent
{
    init(
        testTitle: String = "",
        testInstructions: [HelpSheetContent.Instruction] = [],
        testExamples: [HelpSheetContent.Example] = [],
        testFooter: String = "",
        testCloseButton: HelpSheetContent.CloseButton = HelpSheetContent.CloseButton()
    )
    {
        self.init(
            title: testTitle,
            instructions: testInstructions,
            examples: testExamples,
            footer: testFooter,
            closeButton: testCloseButton
        )
    }
}
