//
//  NSString+Extension.m
//  BJPinChe
//
//  Created by APP_USER on 14-11-19.
//

#import "NSString+Extension.h"

@implementation NSString (Extension)

-(NSMutableAttributedString *)attributedStringFromNString:(NSString *)string
{
    NSString *dian =@"点";
    NSRange range =[string rangeOfString:dian];
    NSMutableAttributedString *attributeString =[[NSMutableAttributedString alloc]initWithString:string];
    [attributeString addAttribute:NSForegroundColorAttributeName value:[UIColor redColor] range:NSMakeRange(0,range.location)];

    return attributeString;
}
+(NSMutableAttributedString *)attributedStringFromNString:(NSString *)string
{
    
    return [string attributedStringFromNString:string];
}


@end
