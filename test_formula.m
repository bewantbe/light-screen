%

%T = 6550;
T = 6504;

if (T < 4000)
    x = -0.2661239*1e9/T/T/T - 0.2343580*1e6/T/T + 0.8776956*1e3/T + 0.179910;
else
    x = -3.0258469*1e9/T/T/T + 2.1070379*1e6/T/T + 0.2226347*1e3/T + 0.240390;
end
if (T < 2222)
    y = -1.1063814*x*x*x - 1.34811020*x*x + 2.18555832*x - 0.20219683;
elseif (T < 4000)
    y = -0.9549476*x*x*x - 1.37418593*x*x + 2.09137015*x - 0.16748867;
else
    y =  3.0817580*x*x*x - 5.87338670*x*x + 3.75112997*x - 0.37001483;
end

x
y

%x = 0.3127
%y = 0.3290

Y=1;

X = Y * x / y;
Z = Y * (1-x-y) / y;

function v = CSRGB(c)
    if (c <= 0.0031308)
        v = 12.92 * c;
    else
        v = 1.055 * c ^ (1/2.4) - 0.055;
    end
end

RGB = zeros(3,1);
RGB(1) = CSRGB( 3.2406*X - 1.5372*Y - 0.4986*Z);
RGB(2) = CSRGB(-0.9689*X + 1.8758*Y + 0.0415*Z);
RGB(3) = CSRGB( 0.0557*X - 0.2040*Y + 1.0570*Z);

RGB

floor(RGB/max(RGB)*255.99)

% vim: set ts=4 sw=4 ss=4
