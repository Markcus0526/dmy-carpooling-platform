//
//  CPTextViewPlaceholder.m
//  Cassius Pacheco
//
//  Created by Cassius Pacheco on 30/01/13.
//  Copyright (c) 2013 Cassius Pacheco. All rights reserved.
//

#import "CPTextViewPlaceholder.h"

@interface CPTextViewPlaceholder()


@end

@implementation CPTextViewPlaceholder

#pragma mark -
#pragma mark Life Cycle method

- (id)initWithCoder:(NSCoder *)aDecoder
{
    if (self = [super initWithCoder:aDecoder]) {
        
        self.placeholderColor = [UIColor lightGrayColor];
        self.originalCorrection = self.autocorrectionType;
        self.originalTextColor = super.textColor;
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(didBeginEditing) name:UITextViewTextDidBeginEditingNotification object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(didEndEditing) name:UITextViewTextDidEndEditingNotification object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(textDidChange:) name:UITextViewTextDidChangeNotification object:self];
    }
    
    return self;
}

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UITextViewTextDidBeginEditingNotification object:self];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UITextViewTextDidEndEditingNotification object:self];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UITextViewTextDidChangeNotification object:self];
}

- (void)layoutSubviews
{
    [super layoutSubviews];
    
    //Fixes iOS 5.x cursor when becomeFirstResponder
    if ([UIDevice currentDevice].systemVersion.floatValue < 6.000000) {
        if (self.isUsingPlaceholder && self.isFirstResponder) {
            self.text = @"";
        }
    }
}

#pragma mark -
#pragma mark Notifications

- (void)didBeginEditing
{
    if (self.isUsingPlaceholder) {
        [self sendCursorToBeginning];
    }
}

- (void)didEndEditing
{
    if (self.text.length == 0) {
        [self setupPlaceholder];
    }
}

- (void)textDidChange:(NSNotification *)notification
{
    //self.text received the placeholder text by CPTex tViewPlaceholder
    if (self.isSettingPlaceholder) {
        return;
    }
    
    if (self.text.length == 0) {
        [self setupPlaceholder];
        return;
    }
    
    if (self.isUsingPlaceholder) {
        self.usingPlaceholder = NO;
        NSRange range = [self.text rangeOfString:self.placeholder options:NSLiteralSearch];
        
        if (range.location != NSNotFound) {
            NSString *newText = [self.text stringByReplacingCharactersInRange:range withString:@""];
            super.textColor = self.originalTextColor;
            super.autocorrectionType = self.originalCorrection;
            
            //User pasted a text equals to placeholder or setText was called
            if ([newText isEqualToString:self.placeholder]) {
                [self sendCursorToEnd];
                //this is necessary for iOS 5.x
            } else if (newText.length == 0) {
                [self setupPlaceholder];
                return;
            }
            
            self.text = newText;
        }
    }
}

- (BOOL)canPerformAction:(SEL)action withSender:(id)sender
{
    if (self.isUsingPlaceholder && action != @selector(paste:)) {
        return NO;
    }
    
    return [super canPerformAction:action withSender:sender];
}

#pragma mark - Getters and Setters

- (void)setAutocorrectionType:(UITextAutocorrectionType)autocorrectionType
{
    [super setAutocorrectionType:autocorrectionType];
    self.originalCorrection = autocorrectionType;
}

- (void)setPlaceholder:(NSString *)placeholder
{
    _placeholder = placeholder;
    
    if (self.isUsingPlaceholder || self.text.length == 0) {
        [self setupPlaceholder];
    }
}

- (void)setTextColor:(UIColor *)textColor
{
    [super setTextColor:textColor];
    self.originalTextColor = textColor;
}

- (void)setSelectedRange:(NSRange)selectedRange
{
    if (self.isUsingPlaceholder) {
        [self sendCursorToBeginning];
    } else {
        [super setSelectedRange:selectedRange];
    }
}

- (void)setSelectedTextRange:(UITextRange *)selectedTextRange
{
    if (self.isUsingPlaceholder) {
        [self sendCursorToBeginning];
    } else {
        [super setSelectedTextRange:selectedTextRange];
    }
}

#pragma mark -
#pragma mark Utilities methods

- (void)setupPlaceholder
{
    super.autocorrectionType = UITextAutocorrectionTypeNo;
    self.usingPlaceholder = YES;
    self.settingPlaceholder = YES;
    self.text = self.placeholder;
    self.settingPlaceholder = NO;
    super.textColor = self.placeholderColor;
    [self sendCursorToBeginning];
}

- (void)sendCursorToBeginning
{
    [self performSelector:@selector(cursorToBeginning) withObject:nil afterDelay:0.01];
}

- (void)cursorToBeginning
{
    super.selectedRange = NSMakeRange(0, 0);
}

- (void)sendCursorToEnd
{
    [self performSelector:@selector(cursorToEnd) withObject:nil afterDelay:0.01];
}

- (void)cursorToEnd
{
    super.selectedRange = NSMakeRange(self.text.length, 0);
}

@end
